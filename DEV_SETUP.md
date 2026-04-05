# LGPlugin — Environnement de développement

## Premier setup (une seule fois)

```bash
cd LGPlugin/
./scripts/setup-env.sh
```

Ce script télécharge automatiquement :
- **Java 21** (Temurin) dans `.tools/java21/`
- **Maven 3.9** dans `.tools/maven/`
- **Paper** (version définie dans `dev.yml`) dans `../server/`
- Crée `eula.txt` et `server.properties` de base

> Tout est dans `.tools/` (ignoré par git). Chaque développeur relance `setup-env.sh` une fois après un clone.

---

## Déployer et démarrer le serveur

```bash
./scripts/deploy-local.sh          # build + copie + start
./scripts/deploy-local.sh --no-run # build + copie uniquement
./scripts/deploy-local.sh --run    # démarrer sans recompiler
```

`deploy-local.sh` est généré par `setup-env.sh` et pointe vers les outils locaux.  
`deploy.sh` fonctionne aussi si Java 21 et Maven sont déjà dans votre PATH.

---

## Depuis IntelliJ IDEA

1. **Run → Edit Configurations → + → Shell Script**
2. Script path : `scripts/deploy-local.sh`
3. Arguments : *(vide pour build+start, ou `--no-run` pour build seul)*
4. Working directory : `$PROJECT_DIR$`

Vous pouvez créer deux configurations : `Deploy & Run` et `Deploy only`.

---

## Configuration (`dev.yml`)

| Clé | Description | Défaut |
|-----|-------------|--------|
| `server_dir` | Chemin du serveur relatif à `LGPlugin/` | `../server` |
| `paper_jar` | Nom du jar Paper | `paper-1.21.11-127.jar` |
| `java_cmd` | Commande Java (chemin complet si besoin) | `java` |
| `memory_mb` | RAM allouée au serveur (Mo) | `1024` |
| `paper_version` | Version Paper pour setup-env | `1.21.1` |
| `paper_build` | Build Paper pour setup-env | `127` |

---

## Architecture

```
minecraft-dev/
├── server/                  ← serveur Minecraft local
│   ├── plugins/             ← le jar est copié ici par deploy.sh
│   ├── paper-1.21.11-127.jar
│   └── eula.txt
└── LGPlugin/                ← projet Maven
    ├── dev.yml              ← config partagée
    ├── scripts/
    │   ├── setup-env.sh     ← setup initial
    │   ├── deploy.sh        ← build + deploy + start
    │   └── deploy-local.sh  ← (généré) wrapper avec .tools/
    ├── .tools/              ← java21 + maven (ignoré git)
    └── src/
```