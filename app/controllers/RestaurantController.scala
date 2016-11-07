package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc.Controller
import play.api.mvc.Action
import dao.RestaurantDAO
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

  /*
  implicit val restaurantWrites: Writes[Restaurant] = (
    (JsPath \ "restaurant_id").write[Int] and
      (JsPath \ "name_restaurant").write[String] and
      (JsPath \ "description").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "admin").write[Int]) (unlift(Restaurant.unapply))
  implicit val restaurantReads: Reads[Restaurant] = (
    (JsPath \ "restaurant_id").read[Int] and
      (JsPath \ "name_restaurant").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "admin").read[Int]) (Restaurant.apply _)





  def save = Action.async(parse.json) { implicit request =>
    val restaurant = request.body.validate[Restaurant]
    val received = restaurant.get
    val cRestaurant = CompleteRestaurant(0, received.name, received.address, received.city, 0, received.phone)
    print("Hola")
    RestaurantDAO.save(cRestaurant) map { res =>
      if (res == "Restaurant saved") {
        saveTables(received.name, received.address)
      }
      Ok(res)
    }
  }
  def saveTables(name: String, address: String) = {
    RestaurantDAO.getByNameAndAddress(name, address) map { res =>
      val r = res.get
      for (i <- 0 to 3) {
        TableRestaurants.save(TableRestaurant(0, r.restaurant_id, 4, true))
      }
    }
  }

*/
}