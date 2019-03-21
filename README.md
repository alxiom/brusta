![Brusta logo](https://user-images.githubusercontent.com/16871455/54772005-d2889200-4c49-11e9-90e7-dce87c9305ea.png)
# Brusta
+ Language-agnostic PyTorch model serving
+ serve JIT compiled PyTorch model in production environment

## Requirements
+ docker == 18.09.1
+ wget == 1.20.1
+ your JIT traced PyTorch model (If you are not familiar with JIT tracing, please refer [JIT Tutorial](https://github.com/hyoungseok/jitTutorial))

## Process Flow
1. run a "build server" to make your PyTorch model server binary
2. load your traced PyTorch model file on the "model server"
3. run the model server

## Details On Build Server
1. create ```Dockerfile-brusta``` on your model-builder machine
```
mkdir -p brusta/docker
wget https://raw.githubusercontent.com/hyoungseok/brusta/master/docker/Dockerfile-brusta -P brusta/docker
```
or simply just clone Brusta repo
```
git clone https://github.com/hyoungseok/brusta.git 
```

2. build and run ```Dockerfile-brusta```
```
cd brusta
docker build -t brusta -f docker/Dockerfile-brusta .
docker run -p 8080:8080 --name brustar -it --rm -v ${PWD}/portal:/home/brusta/portal brusta
```
docker build takes 10-20 minutes in average

3. connect to ```localhost:8080/build``` with web browser and summit model infomation
+ model build takes 10-20 seconds in average
+ resulting ```brusta-server.zip``` file will be located under ```portal``` directory with requested timestamp

## Details On Model Server
1. create ```Dockerfile-brusta-server``` on your model-server machine
```
mkdir -p brusta/docker
wget https://raw.githubusercontent.com/hyoungseok/brusta/master/docker/Dockerfile-brusta-server -P brusta/docker
```
or simply just clone Brusta repo
```
git clone https://github.com/hyoungseok/brusta.git 
```

2. copy your JIT traced model file (```pth``` file) and ```brusta-server.zip``` file to brusta directory
```
cd brusta
cp /PATH/TO/YOUR_BRUSTA_SERVER_BIN.zip brusta-server.zip
cp /PATH/TO/YOUR_PTH_FILE.pth trace_model.pth
```

3. build and run ```Dockerfile-brusta-server```
```
docker build -t brusta-server -f docker/Dockerfile-brusta-server .
docker run -d -p 8080:8080 --name brustar-server --rm -v ${PWD}/logs:/home/brusta/logs brusta-server
```
docker build takes about 5 minutes

## Request Example
request to the model server as follow (suppose your input dimension is 3)
```
curl -X POST -d '{"input":[1.0, 1.0, 1.0]}' localhost:8080/model/predict
```

## Contributors
+ YongRae Jo (dreamgonfly@gmail.com)
+ YoonHo Jo (cloudjo21@gmail.com)
+ GiChang Lee (new.ratsgo@gmail.com)

## Author
+ Alex Kim (hyoungseok.k@gmail.com)
