#!/bin/bash
set -euo pipefail

PROJECT_PATH="/home/pi/projects/corona-project"
ETAG_FILANAME=etag

if [ ! -e "$ETAG_FILANAME" ]; then
    touch "$ETAG_FILANAME"
fi

etag=$(curl -s -I https://covid.ourworldindata.org/data/owid-covid-data.csv | grep etag)
current_etag=$(cd "$PROJECT_PATH"  && cat "$ETAG_FILANAME")

if [[ "$etag" == "$current_etag" ]]; then
  echo 'Etags are the same. Skipping deployment.'
else
  echo "Etags are different $etag vs. $current_etag"
  cd "$PROJECT_PATH" && make deploy > "/home/pi/log/ricotta/$(date -u +"%Y-%m-%dT%H:%M:%S%Z")" 2>&1
  make clean-up
  echo "$etag" > "$ETAG_FILANAME"
fi
