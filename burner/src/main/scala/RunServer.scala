/**
  * Created by Alex on 02/02/2019.
  */

import akka.actor.ActorSystem
import colossus.core.IOSystem
import colossus.protocols.http.{HttpServer, Initializer}
import com.google.inject.{Guice, Injector}

object RunServer {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ioSystem: IOSystem = IOSystem()

  private val port: Int = System.getProperty("port", "9000").toInt
  private val inputDim: Long = System.getProperty("input_dim").toLong
  private val modelPath: String = System.getProperty("model_path")
  private val libraryPath: String = System.getProperty("lib_path")
  System.load(s"${libraryPath}/libModel.so")

  private val injector: Injector = Guice.createInjector()
  private val runEval: RunEval = injector.getInstance(classOf[RunEval])
  private val evalJNI: EvalJNI = injector.getInstance(classOf[EvalJNI])
  private val modelP: Long = evalJNI.loadModel(s"${modelPath}/trace_model.pth")

  def main(args: Array[String]): Unit = {
    println(s"serverStart\u241Btimestamp=${System.currentTimeMillis()}\u241BmodelPointer=${modelP}")

    HttpServer.start("burner", port){initContext =>
      new Initializer(initContext) {
        override def onConnect: RequestHandlerFactory = serverContext => {
          new ManageRequest(serverContext, inputDim, runEval, modelP)
        }
      }
    }
  }

}