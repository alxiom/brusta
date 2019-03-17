# Brusta
+ PyTorch model serving project
+ Serve PyTorch model in production envoronments

## requirements
+ docker == 18.09.1

## clone project
```
git clone https://github.com/hyoungseok/brusta.git
cd brusta
```

## build and run Dockerfile-brusta
```
docker build -t brusta -f docker/Dockerfile-brusta .
docker run -d -p 9000:9000 --name brusta --rm brusta
```

## connect to localhost:9000/build and summit model info

## add your pth file and Brusta result package binary path to Dockerfile-brusta-server

## build and run Dockerfile-brusta-server
