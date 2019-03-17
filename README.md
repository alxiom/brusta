# Brusta
+ PyTorch model serving project
+ Serve PyTorch model in production envoronments

## requirements
+ docker == 18.09.1

## build and run Dockerfile-brusta
```
git clone https://github.com/hyoungseok/brusta.git
cd brusta
docker build -t brusta -f docker/Dockerfile-brusta .
mkdir portal
docker run -p 8080:8080 --name brustar -it --rm --volume ${PWD}/portal:/home/brusta/portal brusta
```

## connect to ```localhost:8080/build``` and summit model info

## add your pth file and Brusta result package binary path to Dockerfile-brusta-server

## build and run Dockerfile-brusta-server
