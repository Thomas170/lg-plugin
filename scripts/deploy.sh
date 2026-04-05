#!/usr/bin/env bash
# =============================================================================
#  deploy.sh  —  Build, déploie et démarre le serveur de test LGPlugin
#
#  Usage (depuis le dossier LGPlugin/) :
#    ./scripts/deploy.sh          → build + copie + démarre le serveur
#    ./scripts/deploy.sh --no-run → build + copie uniquement (pas de start)
#    ./scripts/deploy.sh --run    → démarre le serveur sans recompiler
#
#  Prérequis : dev.yml présent dans LGPlugin/
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
CONFIG="$PROJECT_DIR/dev.yml"

# ─── Lecture de dev.yml (parser YAML minimaliste) ────────────────────────────
read_yml() {
    grep "^$1:" "$CONFIG" | sed 's/.*: *//' | tr -d '"' | tr -d "'" | xargs
}

SERVER_DIR="$(cd "$PROJECT_DIR/$(read_yml server_dir)" && pwd)"
PAPER_JAR="$(read_yml paper_jar)"
JAVA_CMD="$(read_yml java_cmd)"
MEMORY_MB="$(read_yml memory_mb)"

PLUGINS_DIR="$SERVER_DIR/plugins"
JAR_NAME=$(ls "$PROJECT_DIR/target/"*-SNAPSHOT.jar 2>/dev/null | head -1 | xargs basename 2>/dev/null || echo "")

# ─── Couleurs ─────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()    { echo -e "${CYAN}[LG]${NC} $*"; }
success() { echo -e "${GREEN}[LG]${NC} $*"; }
warn()    { echo -e "${YELLOW}[LG]${NC} $*"; }
error()   { echo -e "${RED}[LG]${NC} $*"; exit 1; }

# ─── Arguments ────────────────────────────────────────────────────────────────
DO_BUILD=true
DO_RUN=true

for arg in "$@"; do
    case $arg in
        --no-run) DO_RUN=false ;;
        --run)    DO_BUILD=false ;;
        --help)
            echo "Usage: deploy.sh [--no-run | --run | --help]"
            echo "  (aucun argument) : build + copie + démarre le serveur"
            echo "  --no-run         : build + copie, sans démarrer"
            echo "  --run            : démarre le serveur sans recompiler"
            exit 0 ;;
        *) warn "Argument inconnu : $arg" ;;
    esac
done

# ─── Vérifications ────────────────────────────────────────────────────────────
[[ -f "$CONFIG" ]] || error "dev.yml introuvable dans $PROJECT_DIR"
[[ -d "$SERVER_DIR" ]] || error "Dossier serveur introuvable : $SERVER_DIR (vérifiez server_dir dans dev.yml)"
[[ -d "$PLUGINS_DIR" ]] || { warn "Création du dossier plugins…"; mkdir -p "$PLUGINS_DIR"; }

# ─── Build Maven ──────────────────────────────────────────────────────────────
if $DO_BUILD; then
    info "Build Maven en cours…"
    cd "$PROJECT_DIR"
    mvn package -q -DskipTests 2>&1 | tail -5
    success "Build terminé."

    # Chercher le jar produit (exclure *-shaded si présent)
    JAR_PATH=$(ls "$PROJECT_DIR/target/"*.jar 2>/dev/null | grep -v 'original' | head -1)
    [[ -n "$JAR_PATH" ]] || error "Aucun jar trouvé dans target/ après le build."
    JAR_NAME=$(basename "$JAR_PATH")

    info "Copie de $JAR_NAME vers $PLUGINS_DIR …"
    # Supprimer l'ancienne version du plugin
    rm -f "$PLUGINS_DIR"/LGPlugin*.jar "$PLUGINS_DIR"/lg-plugin*.jar
    cp "$JAR_PATH" "$PLUGINS_DIR/$JAR_NAME"
    success "Plugin déployé : $PLUGINS_DIR/$JAR_NAME"
fi

# ─── Démarrage du serveur ─────────────────────────────────────────────────────
if $DO_RUN; then
    PAPER_PATH="$SERVER_DIR/$PAPER_JAR"
    [[ -f "$PAPER_PATH" ]] || error "Paper introuvable : $PAPER_PATH\n       Lancez scripts/setup-env.sh pour le télécharger."

    info "Démarrage du serveur (mémoire : ${MEMORY_MB}Mo) …"
    info "Dossier : $SERVER_DIR"
    echo ""
    cd "$SERVER_DIR"
    exec "$JAVA_CMD" -Xms${MEMORY_MB}m -Xmx${MEMORY_MB}m -jar "$PAPER_JAR" nogui
fi