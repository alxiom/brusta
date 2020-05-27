# simple burner
simplify overall code
+ independent with `.pth` file
+ keep name `burner`
+ to manage batch / output dimension, see `Model.cpp`

## build image
excute below on current directory to create `libModel.so` file under `/home/brusta`
```
docker build -t brusta -f Dockerfile-brusta .
```

## copy file from temporary container of above image
[link](https://www.youtube.com/watch?v=KtujZdV3G1E)
