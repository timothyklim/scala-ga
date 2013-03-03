import akka.util.Timeout
import scala.concurrent.Future
import akka.actor._
import spray.can.client.HttpClient
import spray.client.HttpConduit
import spray.io._
import spray.util._
import spray.http._
import HttpMethods._
import spray.httpx.encoding.{Gzip, Deflate}
import spray.http.HttpResponse
import spray.httpx.SprayJsonSupport._
import spray.http.HttpHeaders._
import spray.json._
import scala.concurrent.duration._
import scala.collection.immutable._
import ContentType._
import HttpConduit._

object TestGA extends App {
  implicit val timeout: Timeout = 5 seconds span
  implicit val system = ActorSystem()

  val ioBridge = IOExtension(system).ioBridge()
  val httpClient = system.actorOf(Props(new HttpClient(ioBridge)))

  val conduit = system.actorOf(
    props = Props(new HttpConduit(httpClient, "www.googleapis.com", 443, sslEnabled = true)),
    name = "http-conduit"
  )

  val pipeline: HttpRequest => Future[String] = (
    addHeaders(
      `Authorization`(OAuth2BearerToken("ya.wowowowowowow")),
      `Content-Type`(`application/json`))
      ~> encode(Gzip)
      ~> sendReceive(conduit)
      ~> decode(Deflate)
      ~> unmarshal[String]
    )

  val params = HashMap(
    "ids" -> "ga:123121223212",
    "start-date" -> "2013-03-01",
    "end-date" -> "2013-03-02",
    "metrics" -> "ga:visits,ga:newVisits,ga:transactions,ga:transactionRevenue,ga:pageviews,ga:timeOnSite,ga:bounces",
    "dimensions" -> "ga:campaign,ga:source,ga:medium,ga:adContent,ga:keyword")

  val paramsList = for( (key, value) <- params.toList ) yield "%s=%s".format(key, value)

  val query = "?%s" format(paramsList.mkString("&"))

  println("url: %s" format(s"/analytics/v3/data/ga${query}"))

  val response = pipeline(HttpRequest(method = GET, uri = s"/analytics/v3/data/ga${query}"))
  response.map { response =>
    system.shutdown()

    val jsonAst = JsonParser(response)
    println(jsonAst.prettyPrint)

    jsonAst.asJsObject.getFields("columnHeaders") match {
      case collection.mutable.Seq(JsArray(items)) =>
        for (columnHeader <- items) {
          println("columnHeader: %s" format(columnHeader))
        }
    }
  }
}
