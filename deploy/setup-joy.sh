#!/usr/bin/env bash
# One-time setup on joy so Buildkite can build and deploy TrollAttack. Idempotent.
#   sudo deploy/setup-joy.sh
set -euo pipefail
cd "$(dirname "$0")"

apt-get install -y -qq openjdk-8-jdk-headless   # javac/jar for the build step
install -m 755 -o root -g root trollattack-deploy /usr/local/sbin/trollattack-deploy
install -d -m 755 /opt/trollattack /opt/trollattack/releases
visudo -cf sudoers-buildkite
install -m 440 -o root -g root sudoers-buildkite /etc/sudoers.d/trollattack-buildkite
install -m 644 trollattack.service /etc/systemd/system/trollattack.service
systemctl daemon-reload
systemctl enable trollattack.service
echo "Done. The first Buildkite deploy installs a release into /opt/trollattack/current."
