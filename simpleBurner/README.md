# simple burner
simplify brusta code

## requirements
+ docker == 19.03.8

## compatiblility
+ `torch.jit.trace` or `torch.jit.script` result `.pth` file (PyTorch >= 1.2.0, recommand 1.5.0)
+ in your application code, create a `burner` package and make `EvalJNI` class under the package to call `EvalJNI` function (see `src/EvalJNI.h`)
+ SEE `src/ModelSpec.hpp` AND SET BATCH / OUTPUT DIMENSION!

## build image
excute below uner `simpleBurner` directory to create `libModel.so` file under `/home/brusta`
```
cd simpleBurner
docker build -t brusta -f Dockerfile-brusta .
```

## copy file from temporary container of above image
after build `brusta` image, run below
```
cd simpleBurner
./copy-from-docker.sh
```
you will get `libModel.so` in current directory
