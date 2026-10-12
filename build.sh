#!/usr/bin/env bash
set -eo pipefail
cd "$(dirname "$0")"
mkdir -p logs .fuzz gradle/wrapper

W=gradle/wrapper/gradle-wrapper.jar
if [ ! -f "$W" ]; then
  wget -q -O "$W" https://raw.githubusercontent.com/gradle/gradle/v8.12.1/gradle/wrapper/gradle-wrapper.jar
fi

S=src/main/java/dev/kiuox/pingback
javac -d .fuzz $S/config/PingbackConfig.java $S/util/QuoteCleaner.java $S/util/TextUtil.java $S/util/Preview.java fuzz/Fuzz.java
java -cp .fuzz Fuzz

chmod +x gradlew
./gradlew --no-daemon build 2>&1 | tee logs/build.log

JAR="$(find build/libs -name 'pingback-*.jar' ! -name '*-sources.jar' | head -n1)"
[ -n "$JAR" ] || { echo "build failed, see logs/build.log"; exit 1; }

DEST="$HOME/storage/downloads"
[ -d "$DEST" ] || DEST="$HOME"
cp -f "$JAR" "$DEST/$(basename "$JAR")"
echo "Jar: $DEST/$(basename "$JAR")"
