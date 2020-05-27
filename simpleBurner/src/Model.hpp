#ifndef MODEL_H
#define MODEL_H

namespace pytorch {
    class Model {
        public:
            Model();
            Model(const char* modelName);
            ~Model();
            torch::jit::script::Module module;
            std::vector<float> predict(std::vector<long> x);
    };

    class Eval {
        public:
            Eval();
            ~Eval();
            long loadModel(const char* modelName);
            float* evaluate(long pModel, long* x, int xSize);
    };
}

#endif
