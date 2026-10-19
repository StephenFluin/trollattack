#!/usr/bin/env bash
# Compile the game into dist/trollattack.jar. Needs a JDK (25 or newer).
#   scripts/build.sh
set -euo pipefail
cd "$(dirname "$0")/.."

rm -rf build dist
mkdir -p build/classes dist
# BinaryCodedDecimal* are JUnit 3 experiments that nothing in the game uses.
find TrollAttack -name '*.java' ! -name 'BinaryCodedDecimal*.java' | sort > build/sources.txt
# Small heap and low priority: this also runs on joy next to the live sites.
nice -n 10 javac -J-Xmx256m -nowarn -encoding ISO-8859-1 --release 25 \
    -cp smack.jar:smackx.jar -d build/classes @build/sources.txt
jar cfm dist/trollattack.jar manifest -C build/classes .
echo "Built dist/trollattack.jar ($(wc -c < dist/trollattack.jar) bytes, $(wc -l < build/sources.txt) sources)"
