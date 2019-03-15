#!/bin/bash

mkdir -p $HOME/temp

binPath=./burner-0.1.0/bin/burner

source run-server.config

${binPath} -Dlogback.configurationFile=logback-http.xml -Dport=${port} -Dinput_dim=${inputDim} -Dmodel_path=${modelPath} -Dlib_path=${libPath} -Dfile.encoding=UTF-8 -Djava.io.tmpdir=$HOME/temp -J-Xms${memory}M -J-Xmx${memory}M -J-XX:+UseG1GC -J-server
