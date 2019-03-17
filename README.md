# Brusta
+ PyTorch model serving project
+ Serve PyTorch model in production envoronments

## requirements
+ docker == 18.09.1

## clone project
```
git clone https://github.com/hyoungseok/brusta.git
cd brusta
mkdir portal
```

## build and run Dockerfile-brusta
```
docker build -t brusta -f docker/Dockerfile-brusta .
docker run -p 8080:8080 --name brustar -it --rm --volume portal:/tmp brusta
```

## connect to localhost:8080/build and summit model info

## add your pth file and Brusta result package binary path to Dockerfile-brusta-server

## build and run Dockerfile-brusta-server
