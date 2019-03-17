/**
  * Created by Alex on 16/03/2019.
  */

import akka.actor.ActorSystem
import colossus.core.IOSystem
import colossus.protocols.http.{HttpServer, Initializer}
import com.google.inject.{Guice, Injector}

object RunServer {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ioSystem: IOSystem = IOSystem()

  private val port: Int = 9000
  private val injector: Injector = Guice.createInjector()
  private val summitService = injector.getInstance(classOf[SummitService])

  def main(args: Array[String]): Unit = {
    println(s"serverStart\u241Btimestamp=${System.currentTimeMillis()}")

    HttpServer.start("lever", port){initContext =>
      new Initializer(initContext) {
        override def onConnect: RequestHandlerFactory = serverContext => {
          new ManageRequest(serverContext, summitService)
        }
      }
    }
  }

}