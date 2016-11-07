package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc.Controller
import play.api.mvc.Action
import dao.CityDAO
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CityController @Inject() (cityDAO: CityDAO) extends Controller {

  def getAll = Action.async {
    cityDAO.all map { city =>
      val json = Json.toJson(city)
      Ok(json)
    }
  }
  def getById(id: Long) = Action.async {
    cityDAO.get(id) map { city =>
      val json = Json.toJson(city)
      Ok(json)
    }
  }
}