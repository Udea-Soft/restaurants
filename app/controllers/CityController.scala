package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc.Controller
import play.api.libs.json.Writes
import model.City
import play.api.libs.json.JsPath
import play.api.libs.functional.syntax._
import play.api.mvc.Action
import dao.Cities
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CityController @Inject extends Controller {
  implicit val restaurantWrites: Writes[City] = (
    (JsPath \ "city_id").write[Int] and
    (JsPath \ "name").write[String])(unlift(City.unapply))
  def getAll = Action.async {
    Cities.list map { city =>
      val json = Json.toJson(city)
      Ok(json)
    }
  }
}