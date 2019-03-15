#include <iostream>
#include <torch/script.h>
#include "Model.hpp"
#include "ModelIO.hpp"
#include "EvalJNI.h"
#define BATCH_SIZE 1

using namespace std;

namespace pytorch {
    Model::Model () {}
    Model::Model (const char* modelName) {
        this -> module = torch::jit::load(modelName);
    }
    Model::~Model () {}
    vector<float> Model::predict (vector<float> x) {
        at::Tensor inputVector = torch::from_blob(&x[0], {BATCH_SIZE, INPUT_FEATURE_SIZE}, at::kFloat).clone();
        vector<torch::jit::IValue> inputTensor;
        inputTensor.push_back(inputVector);
        at::Tensor outputTensor = this -> module -> forward(inputTensor).toTensor();
        vector<float> outputVector(outputTensor.data<float>(), outputTensor.data<float>() + outputTensor.numel());
        return outputVector;
    }

    Eval::Eval () {}
    Eval::~Eval () {}
    long Eval::loadModel (const char* modelName) {
        Model* pModel = new Model(modelName);
        return long (pModel);
    }
    float* Eval::evaluate(long pModel, float* x) {
        vector<float> vectorX (x, x + BATCH_SIZE * INPUT_FEATURE_SIZE);
        vector<float> vectorY = ((Model*)pModel) -> Model::predict(vectorX);
    	static float y[OUTPUT_SIZE];
        for (int i = 0; i < BATCH_SIZE * OUTPUT_SIZE; ++i) {
            y[i] = vectorY[i];
        }
        return y;
    }
}

JNIEXPORT jlong JNICALL Java_EvalJNI_loadModel
  (JNIEnv * env, jobject thisObj, jstring modelName) {
  const char* modelNameString = env -> GetStringUTFChars(modelName, 0);
  pytorch::Eval eval = pytorch::Eval();
  long pModel = eval.loadModel(modelNameString);
  env -> ReleaseStringUTFChars(modelName, modelNameString);
  return pModel;
}

JNIEXPORT jfloatArray JNICALL Java_EvalJNI_evaluate
  (JNIEnv * env, jobject thisObj, jlong pModel, jfloatArray x) {
    jfloatArray result = env -> NewFloatArray(OUTPUT_SIZE);
    jfloat* jX = env -> GetFloatArrayElements(x, 0);

    pytorch::Eval eval = pytorch::Eval();
    float* y = eval.evaluate(pModel, jX);

    env -> ReleaseFloatArrayElements(x, jX, 0);
    env -> SetFloatArrayRegion(result, 0, OUTPUT_SIZE, y);
    return result;
}
