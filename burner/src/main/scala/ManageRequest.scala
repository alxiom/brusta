/**
  * Created by Alex on 02/02/2019.
  */

import colossus.core.ServerContext
import colossus.protocols.http.HttpMethod._
import colossus.protocols.http.UrlParsing._
import colossus.protocols.http._
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler
import org.json4s._
import org.json4s.native.JsonMethods.parse
import javax.inject.{Inject, Singleton}

@Singleton
class ManageRequest @Inject()(context: ServerContext,
                              inputDim: Long,
                              runEval: RunEval,
                              modelP: Long) extends RequestHandler(context) {

  implicit val formats: DefaultFormats.type = DefaultFormats

  private val log = com.typesafe.scalalogging.Logger(getClass)
  private val jsonHeader = HttpHeader("Content-type", "application/json")

  override def handle: PartialHandler[Http] = {
    case req @ Get on Root =>
      log.info(s"aliveCheck")
      Callback.successful(req.ok("""{"status": "ok", "code": 200}""", HttpHeaders(jsonHeader)))
    case req @ Post on Root / "model" / "predict" =>
      log.info(s"predictRequest\u241BrequestString=${req.toString.take(100)}")
      val bytes = req.body.bytes
      if (bytes.isEmpty) {
        val emptyBodyErrorJson = """{"status": "error", "code": 400, "message": "empty body"}"""
        log.info(s"returnFail\u241BemptyBody=${bytes.isEmpty}")
        Callback.successful(req.badRequest(emptyBodyErrorJson, HttpHeaders(jsonHeader)))
      } else {
        val body = parse(bytes.utf8String)
        val parseInputVector = body \\ "input"
        val inputVectorO =  parseInputVector match {
          case _: JArray => Some(parseInputVector.asInstanceOf[JArray].arr.map(x => x.extract[Float]))
          case _ => None
        }
        if (inputVectorO.nonEmpty) {
          val inputVector = inputVectorO.get
          if (inputVector.length == inputDim) {
            val result = runEval(inputVector, modelP)
            val responseJson = s"""{"status": "ok", "code": 200, "data": {"result": "${result}"}}"""
            log.info(s"returnSuccess\u241BresponseJson=${responseJson}")
            Callback.successful(req.ok(responseJson, HttpHeaders(jsonHeader)))
          } else {
            val wrongInputErrorJson = """{"status": "error", "code": 400, "message": "vector dimension mismatched"}"""
            log.info(s"returnFail\u241BmismatchDim\u241BrequestDim=${inputVector.length}\u241BdefinedDim=${inputDim}")
            Callback.successful(req.badRequest(wrongInputErrorJson, HttpHeaders(jsonHeader)))
          }
        } else {
          val emptyInputErrorJson = """{"status": "error", "code": 400, "message": "empty input"}"""
          log.info(s"returnFail\u241BemptyInput=${inputVectorO.isEmpty}")
          Callback.successful(req.badRequest(emptyInputErrorJson, HttpHeaders(jsonHeader)))
        }
      }
 }

}
