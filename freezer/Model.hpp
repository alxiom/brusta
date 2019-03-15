#ifndef MODEL_H
#define MODEL_H

namespace pytorch {
    class Model {
        public:
            Model();
            Model(const char* modelName);
            ~Model();
            std::shared_ptr<torch::jit::script::Module> module;
            std::vector<float> predict(std::vector<float> x);
    };

    class Eval {
        public:
            Eval();
            ~Eval();
            long loadModel(const char* modelName);
            float* evaluate(long pModel, float* x);
    };
}

#endif
