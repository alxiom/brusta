#!/bin/bash

./stop-server.sh
rm -rf burner-0.1.0
unzip burner-0.1.0.zip
./start-server.sh
