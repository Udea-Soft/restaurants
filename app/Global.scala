import play.api._
import play.api.mvc._

import play.api.Logger
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Global extends WithFilters() {
  implicit class RichResult (result: Result) {
  def enableCors =  result.withHeaders(
    "Access-Control-Allow-Origin" -> "*"
    , "Access-Control-Allow-Methods" -> "OPTIONS, GET, POST, PUT, DELETE, HEAD"   // OPTIONS for pre-flight
    , "Access-Control-Allow-Headers" -> "Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With" //, "X-My-NonStd-Option"
    , "Access-Control-Allow-Credentials" -> "true"
  )
}
}
