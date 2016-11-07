package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc.Controller
import play.api.mvc.Action
import dao.RestaurantDAO
import models.Restaurant
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class RestaurantController @Inject()(restaurantDAO: RestaurantDAO) extends Controller {

  def getAll = Action.async {
    restaurantDAO.all map { restaurant =>
      val json = Json.toJson(restaurant)
      Ok(json)
    }
  }

  def getById(id: Long) = Action.async {
    restaurantDAO.get(id) map { restaurant =>
      val json = Json.toJson(restaurant)
      Ok(json)
    }
  }

  def searchByName(name: String) = Action.async {
    restaurantDAO.getByName(name) map { restaurant =>
      val json = Json.toJson(restaurant)
      Ok(json)
    }
  }

  def insert = Action.async(parse.json) { implicit request =>

    val received = request.body.validate[Restaurant].get
    val receivedRestaurant = Restaurant(None, received.name_restaurant, received.description, received.email, received.admin)
    restaurantDAO.save(receivedRestaurant) map { res =>
      Ok(res)
    }
  }
}