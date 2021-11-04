#!/bin/bash
cd /home/pi/projects/corona-project && make deploy > /home/pi/log/ricotta/$(date -u +"%Y-%m-%dT%H:%M:%S%Z") 2>&1
make clean-up

