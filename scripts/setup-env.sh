#!/usr/bin/env bash
# =============================================================================
#  setup-env.sh  —  Configure l'environnement de développement LGPlugin
#
#  Ce script télécharge et installe dans le projet :
#    • Java 21 (Temurin, via archives officielles)
#    • Maven 3.9 (si maven n'est pas dispo ou < 3.8)
#    • Paper (version définie dans dev.yml)
#    • eula.txt acceptée automatiquement dans le dossier serveur
#
#  Usage (depuis le dossier LGPlugin/) :
#    ./scripts/setup-env.sh
#
#  Tout est installé dans  LGPlugin/.tools/  (ignoré par git).
#  deploy.sh utilisera automatiquement ces outils s'ils sont présents.
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
CONFIG="$PROJECT_DIR/dev.yml"
TOOLS_DIR="$PROJECT_DIR/.tools"

# ─── Couleurs ─────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()    { echo -e "${CYAN}[setup]${NC} $*"; }
success() { echo -e "${GREEN}[setup]${NC} $*"; }
warn()    { echo -e "${YELLOW}[setup]${NC} $*"; }
error()   { echo -e "${RED}[setup]${NC} $*"; exit 1; }

# ─── Lecture dev.yml ──────────────────────────────────────────────────────────
read_yml() { grep "^$1:" "$CONFIG" | sed 's/.*: *//' | tr -d '"' | tr -d "'" | xargs; }

SERVER_REL="$(read_yml server_dir)"
SERVER_DIR="$(cd "$PROJECT_DIR/$SERVER_REL" 2>/dev/null || { mkdir -p "$PROJECT_DIR/$SERVER_REL"; cd "$PROJECT_DIR/$SERVER_REL"; } && pwd)"
PAPER_VERSION="$(read_yml paper_version)"
PAPER_BUILD="$(read_yml paper_build)"
PAPER_JAR="$(read_yml paper_jar)"

mkdir -p "$TOOLS_DIR" "$SERVER_DIR/plugins"

# ─── Détection OS / arch ──────────────────────────────────────────────────────
OS="$(uname -s)"
ARCH="$(uname -m)"

case "$OS" in
    Darwin) JDK_OS="mac" ;;
    Linux)  JDK_OS="linux" ;;
    MINGW*|MSYS*|CYGWIN*) JDK_OS="windows" ;;
    *) error "OS non supporté : $OS" ;;
esac

case "$ARCH" in
    x86_64|amd64) JDK_ARCH="x64" ;;
    aarch64|arm64) JDK_ARCH="aarch64" ;;
    *) error "Architecture non supportée : $ARCH" ;;
esac

# ─── Java 21 ──────────────────────────────────────────────────────────────────
JAVA_DIR="$TOOLS_DIR/java21"
JAVA_BIN="$JAVA_DIR/bin/java"

if [[ -x "$JAVA_BIN" ]]; then
    success "Java 21 déjà présent ($JAVA_BIN)"
else
    info "Téléchargement de Java 21 (Temurin, $JDK_OS/$JDK_ARCH)…"

    if [[ "$JDK_OS" == "windows" ]]; then
        EXT="zip"; CONTENT_TYPE="zip"
    else
        EXT="tar.gz"; CONTENT_TYPE="tar.gz"
    fi

    JDK_URL="https://api.adoptium.net/v3/binary/latest/21/ga/${JDK_OS}/${JDK_ARCH}/jdk/hotspot/normal/eclipse?project=jdk"
    JDK_ARCHIVE="$TOOLS_DIR/jdk21.$EXT"

    curl -L -o "$JDK_ARCHIVE" "$JDK_URL" || error "Échec du téléchargement Java 21"

    mkdir -p "$JAVA_DIR"
    if [[ "$EXT" == "tar.gz" ]]; then
        tar -xzf "$JDK_ARCHIVE" -C "$JAVA_DIR" --strip-components=1
    else
        unzip -q "$JDK_ARCHIVE" -d "$JAVA_DIR"
        # Sur Windows, le zip contient un sous-dossier jdk-21.x.x
        INNER=$(ls "$JAVA_DIR" | head -1)
        if [[ -d "$JAVA_DIR/$INNER/bin" ]]; then
            mv "$JAVA_DIR/$INNER"/* "$JAVA_DIR/" && rmdir "$JAVA_DIR/$INNER"
        fi
    fi
    rm -f "$JDK_ARCHIVE"
    success "Java 21 installé dans $JAVA_DIR"
fi

# Mettre à jour le java_cmd dans deploy.sh si nécessaire
JAVA_CMD_IN_TOOLS="$JAVA_BIN"

# ─── Maven ────────────────────────────────────────────────────────────────────
MVN_DIR="$TOOLS_DIR/maven"
MVN_BIN="$MVN_DIR/bin/mvn"
MAVEN_VERSION="3.9.9"

if [[ -x "$MVN_BIN" ]]; then
    success "Maven déjà présent ($MVN_BIN)"
else
    info "Téléchargement de Maven $MAVEN_VERSION…"
    MVN_URL="https://downloads.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz"
    MVN_ARCHIVE="$TOOLS_DIR/maven.tar.gz"
    curl -L -o "$MVN_ARCHIVE" "$MVN_URL" || error "Échec du téléchargement Maven"
    mkdir -p "$MVN_DIR"
    tar -xzf "$MVN_ARCHIVE" -C "$MVN_DIR" --strip-components=1
    rm -f "$MVN_ARCHIVE"
    success "Maven $MAVEN_VERSION installé dans $MVN_DIR"
fi

# ─── Paper ────────────────────────────────────────────────────────────────────
PAPER_PATH="$SERVER_DIR/$PAPER_JAR"

if [[ -f "$PAPER_PATH" ]]; then
    success "Paper déjà présent ($PAPER_PATH)"
else
    info "Téléchargement de Paper $PAPER_VERSION build $PAPER_BUILD…"
    PAPER_URL="https://api.papermc.io/v2/projects/paper/versions/$PAPER_VERSION/builds/$PAPER_BUILD/downloads/$PAPER_JAR"
    curl -L -o "$PAPER_PATH" "$PAPER_URL" || error "Échec du téléchargement Paper"
    success "Paper téléchargé : $PAPER_PATH"
fi

# ─── eula.txt ─────────────────────────────────────────────────────────────────
EULA="$SERVER_DIR/eula.txt"
if [[ ! -f "$EULA" ]]; then
    echo "eula=true" > "$EULA"
    success "eula.txt créé (eula=true)"
else
    sed -i.bak 's/eula=false/eula=true/' "$EULA" && rm -f "$EULA.bak"
    success "eula.txt : eula=true"
fi

# ─── server.properties minimal ────────────────────────────────────────────────
SP="$SERVER_DIR/server.properties"
if [[ ! -f "$SP" ]]; then
    cat > "$SP" <<EOF
online-mode=false
gamemode=adventure
difficulty=peaceful
spawn-protection=0
max-players=20
EOF
    success "server.properties créé"
fi

# ─── Générer un wrapper deploy-local.sh qui utilise les outils locaux ─────────
WRAPPER="$PROJECT_DIR/scripts/deploy-local.sh"
cat > "$WRAPPER" <<WRAPPER_EOF
#!/usr/bin/env bash
# Wrapper généré par setup-env.sh — utilise Java et Maven du dossier .tools/
export JAVA_HOME="$JAVA_DIR"
export PATH="$JAVA_DIR/bin:$MVN_DIR/bin:\$PATH"
exec "\$(dirname "\$0")/deploy.sh" "\$@"
WRAPPER_EOF
chmod +x "$WRAPPER"

# ─── Résumé ───────────────────────────────────────────────────────────────────
echo ""
echo -e "${GREEN}════════════════════════════════════════${NC}"
echo -e "${GREEN}  Environnement prêt !${NC}"
echo -e "${GREEN}════════════════════════════════════════${NC}"
echo ""
echo -e "  Java 21  : ${CYAN}$JAVA_BIN${NC}"
echo -e "  Maven    : ${CYAN}$MVN_BIN${NC}"
echo -e "  Paper    : ${CYAN}$PAPER_PATH${NC}"
echo -e "  Serveur  : ${CYAN}$SERVER_DIR${NC}"
echo ""
echo -e "  Pour déployer et démarrer le serveur :"
echo -e "  ${YELLOW}./scripts/deploy-local.sh${NC}"
echo ""
echo -e "  Ou depuis votre IDE, configurez un Run/Debug Configuration :"
echo -e "  ${YELLOW}bash scripts/deploy-local.sh --no-run${NC}  (build seul)"
echo ""