#!/usr/bin/env bash
# Buildkite deploy step, run by the buildkite-agent user on joy. Hands the jar built
# earlier in this build to the root-owned installer, which restarts the game and
# rolls back if it doesn't answer on port 4000.
set -euo pipefail

WORK=$(mktemp -d)
trap 'rm -rf "$WORK"' EXIT

echo "--- :package: Downloading build"
buildkite-agent artifact download dist/trollattack.jar "$WORK"
chmod 755 "$WORK" "$WORK/dist"
chmod 644 "$WORK/dist/trollattack.jar"

echo "--- :rocket: Installing ${BUILDKITE_COMMIT:0:12}"
sudo /usr/local/sbin/trollattack-deploy "$WORK/dist/trollattack.jar" "$BUILDKITE_COMMIT"
