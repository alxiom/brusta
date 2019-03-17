# Brusta
+ PyTorch model serving project
+ serve PyTorch model in production envoronments

## Requirements
+ docker == 18.09.1

## On Build Server
1. build and run ```Dockerfile-brusta```
```
git clone https://github.com/hyoungseok/brusta.git
cd brusta
docker build -t brusta -f docker/Dockerfile-brusta .
docker run -p 8080:8080 --name brustar -it --rm -v ${PWD}/portal:/home/brusta/portal brusta
```

2. connect to ```localhost:8080/build``` and summit model infomation
+ build takes 10-20 seconds in average

## On Model Server
1. copy your ```pth``` file and Brusta result package binary to brusta project directory
```
cp /PATH/TO/YOUR_BRUSTA_SERVER_BIN.zip brusta-server.zip
cp /PATH/TO/YOUR_PTH_FILE.pth trace_model.pth
```

2. build and run ```Dockerfile-brusta-server```
```
docker build -t brusta-server -f docker/Dockerfile-brusta-server .
docker run -d -p 8080:8080 --name brustar-server --rm -v ${PWD}/logs:/home/brusta/logs brusta-server
```
