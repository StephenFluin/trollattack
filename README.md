# TrollAttack

A telnet MUD in Java, imitating SMAUG-style muds. (c) 2005 Stephen Fluin.

## Build and run locally

Needs a JDK (25 or newer).

```
scripts/build.sh     # compiles to dist/trollattack.jar
scripts/run.sh       # listens on telnet port 4000, using Areas/, Classes/ and Players/ here
```

## Deploys

Buildkite (`.buildkite/pipeline.yml`) builds every push on joy and deploys the default
branch: `deploy/deploy.sh` hands the jar to `/usr/local/sbin/trollattack-deploy`, which
installs it under `/opt/trollattack/releases/`, points `/opt/trollattack/current` at it,
restarts `trollattack.service`, and rolls back if the game doesn't answer on port 4000.

Live game data (areas, classes, player files) stays in `/home/stephen/TrollAttack/bin`
on joy and isn't touched by deploys. One-time server setup: `sudo deploy/setup-joy.sh`.
