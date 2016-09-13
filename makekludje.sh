#!/bin/bash
set -e

which gradle || (echo 'gradle not found' && exit 1)
which grep || (echo 'grep not found' && exit 1)

THERE="$(pwd)"
HERE=$( dirname "${BASH_SOURCE[0]}" )
cd ${HERE}
BLDVER=="$(grep -F -m 1 'version = ' build.gradle)"; BLDVER="${BLDVER#*\'}"; BLDVER="${BLDVER%\'*}"
DOCDIR="$HERE/docs/artefacts/api/$BLDVER"

echo "CODE VERSION = $BLDVER"

gradle -version > ${HERE}/lastbuild_info.txt

gradle clean build check jacoco

function copyDocs() {
  echo "copying docs from $HERE/$1/build/docs/javadoc/. to $DOCDIR/$2"
  if [ -d "$DOCDIR/$2" ]; then rm -Rf "$DOCDIR/$2"; fi
  mkdir -p "$DOCDIR/$2"
  cp -a "$HERE/$1/build/docs/javadoc/." "$DOCDIR/$2"
}

if [[ ${BLDVER} == *"SNAPSHOT"* ]]; then
  echo "Skipping documentation assembly for SNAPSHOT build"
else
  echo "Documenting latest API in $DOCDIR"
  copyDocs kludje-core core
  copyDocs kludje-annotation annotation
fi

cd ${THERE}
