/**
  * Created by Alex on 17/03/2019.
  */

import colossus.core.ServerContext
import colossus.protocols.http.HttpMethod._
import colossus.protocols.http.UrlParsing._
import colossus.protocols.http._
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler
import org.json4s._
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ManageRequest @Inject()(context: ServerContext, summitService: SummitService) extends RequestHandler(context) {

  implicit val formats: DefaultFormats.type = DefaultFormats

  private val log = com.typesafe.scalalogging.Logger(getClass)
  private val jsonHeader = HttpHeader("Content-type", "application/json")
  private val htmlHeader = HttpHeader("content-type", "text/html")
  private val form = scala.io.Source.fromResource("form.html").getLines.mkString
  private val submit = scala.io.Source.fromResource("submit.html").getLines.mkString

  override def handle: PartialHandler[Http] = {
    case req @ Get on Root =>
      log.info(s"aliveCheck")
      Callback.successful(req.ok("""{"status": "ok", "code": 200}""", HttpHeaders(jsonHeader)))
    case req @ Get on Root / "build" =>
      Callback.successful(req.ok(form, HttpHeaders(htmlHeader)))
    case req @ Post on Root / "submit" =>
      log.info(s"buildRequest\u241BrequestString=${req.toString.take(100)}")
      val bytes = req.body.bytes
      if (bytes.isEmpty) {
        val emptyBodyErrorJson = """{"status": "error", "code": 400, "message": "empty body"}"""
        log.info(s"returnFail\u241BemptyBody=${bytes.isEmpty}")
        Callback.successful(req.badRequest(emptyBodyErrorJson, HttpHeaders(jsonHeader)))
      } else {
        val body = req.body.bytes.utf8String
        val parameterMap = body.split("&").map(x => {
          val s = x.split("=")
          (s.head, s.last)
        }).toMap
        if (parameterMap.values.forall(x => x.nonEmpty)) {
          Future.successful(summitService(parameterMap))
          Callback.successful(req.ok(submit, HttpHeaders(htmlHeader)))
        } else {
          val emptyInputErrorJson = """{"status": "error", "code": 400, "message": "empty input"}"""
          log.info(s"returnFail\u241BparameterMap=${parameterMap}")
          Callback.successful(req.badRequest(emptyInputErrorJson, HttpHeaders(jsonHeader)))
        }
      }
  }

}
