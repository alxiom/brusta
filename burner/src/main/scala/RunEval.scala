/**
  * Created by Alex on 03/02/2019.
  */

import javax.inject.{Inject, Singleton}

@Singleton
class RunEval @Inject()(evalJNI: EvalJNI) {

  def apply(inputVector: List[Float], modelP: Long): String = {
      evalJNI.evaluate(modelP, inputVector.toArray).mkString(",")
  }

}