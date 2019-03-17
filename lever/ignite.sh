#!/bin/bash

now=$(date +'%Y-%m-%dT%H:%M:%S')
projectPath=$(dirname "${BASH_SOURCE[0]}")/..

mkdir ${projectPath}/launcher/build
cd ${projectPath}/launcher/build
cmake -DCMAKE_PREFIX_PATH=${projectPath}/launcher/libtorch ..
make

mkdir ${projectPath}/burner/lib
mv ${projectPath}/launcher/build/libModel.so ${projectPath}/burner/lib
cd ${projectPath}/burner
sbt universal:packageBin

mkdir /tmp/dt=${now}
mv ${projectPath}/burner/target/universal/burner-0.1.0.zip /tmp/dt=${now}/brusta-server.zip
echo ${now} brusta-server is created