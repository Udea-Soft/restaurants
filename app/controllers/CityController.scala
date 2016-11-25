package controllers

import javax.inject.Singleton
import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import dao.CityDAO
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CityController @Inject() (cityDAO: CityDAO) extends Controller {

  def getAll = Action.async {
    cityDAO.all map { city =>
      val json = Json.toJson(city)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def getById(id: Long) = Action.async {
    cityDAO.get(id) map { city =>
      val json = Json.toJson(city)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
}