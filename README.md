# Brusta
+ PyTorch model serving project
+ Serve PyTorch model in production envoronments

## requirements
+ docker == 18.09.1

## [On Build Server] Build and run Dockerfile-brusta
```
git clone https://github.com/hyoungseok/brusta.git
cd brusta
docker build -t brusta -f docker/Dockerfile-brusta .
docker run -p 8080:8080 --name brustar -it --rm --volume ${PWD}/portal:/home/brusta/portal brusta
```

## [On Build Server] Connect to ```localhost:8080/build``` and summit model info
+ build takes 10-20 seconds in average

## [On Model Server] Copy your pth file and Brusta result package binary to brusta directory
```
cp /PATH/TO/YOUR_BRUSTA_SERVER_BIN.zip brusta-server.zip
cp /PATH/TO/YOUR_PTH_FILE.pth trace_model.pth
```

## [On Model Server] Build and run Dockerfile-brusta-server
```
docker build -t brusta-server -f docker/Dockerfile-brusta-server .
docker run -d -p 8080:8080 --name brustar-server --rm --volumn ${PWD}/logs:/home/brusta/logs brusta-server
```
