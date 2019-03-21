![Brusta logo](https://user-images.githubusercontent.com/16871455/54768632-e7adf280-4c42-11e9-97a7-c75ccb2c957d.png)
--
+ PyTorch model serving project
+ serve PyTorch model in production envoronments

## Requirements
+ docker == 18.09.1
+ wget == 1.20.1

## On Build Server
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

## On Model Server
1. create ```Dockerfile-brusta-server``` on your model-server machine
```
mkdir -p brusta/docker
wget https://raw.githubusercontent.com/hyoungseok/brusta/master/docker/Dockerfile-brusta-server -P brusta/docker
```
or simply just clone Brusta repo
```
git clone https://github.com/hyoungseok/brusta.git 
```

2. copy your ```pth``` file and ```brusta-server.zip``` file to brusta directory
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
request to the model server as follow
```
curl -X POST -d '{"input":[1.0, 1.0, 1.0]}' localhost:8080/model/predict
```

## Contributors
+ YongRae Jo (dreamgonfly@gmail.com)
+ YoonHo Jo (cloudjo21@gmail.com)
+ GiChang Lee (new.ratsgo@gmail.com)

## Author
+ Alex Kim (hyoungseok.k@gmail.com)
