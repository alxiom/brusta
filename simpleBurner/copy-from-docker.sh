#!/bin/bash

docker create -ti --name dummy brusta bash
docker cp dummy:/home/brusta/libModel.so ./
docker rm -f dummy
