#!/usr/bin/env bash
# Run a local build on telnet port 4000, using the area, class and player files in
# this checkout:  scripts/build.sh && scripts/run.sh   (then: telnet localhost 4000)
set -euo pipefail
cd "$(dirname "$0")/.."
exec java -Xmx96m -XX:+UseSerialGC -jar dist/trollattack.jar
