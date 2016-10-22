package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc.Controller
import play.api.libs.json.Writes
import model.CompleteRestaurant
import play.api.libs.json.JsPath
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads
import model.Restaurant
import play.api.mvc.Action
import dao.Restaurants
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import dao.TableRestaurants
import model.TableRestaurant
import play.mvc.Result
import scala.concurrent.Future

@Singleton
class RestaurantsController @Inject extends Controller {
  implicit val restaurantWrites: Writes[CompleteRestaurant] = (
    (JsPath \ "restaurant_id").write[Long] and
    (JsPath \ "name").write[String] and
    (JsPath \ "address").write[String] and
    (JsPath \ "city").write[Int] and
    (JsPath \ "score").write[Double] and
    (JsPath \ "phone").write[String])(unlift(CompleteRestaurant.unapply))
  implicit val restaurantReads: Reads[Restaurant] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "address").read[String] and
    (JsPath \ "city").read[Int] and
    (JsPath \ "phone").read[String])(Restaurant.apply _)

  def getAll = Action.async {
    Restaurants.list map { restaurant =>
      val json = Json.toJson(restaurant)
      Ok(json)
    }
  }

  def getById(id: Long) = Action.async {
    Restaurants.get(id) map { restaurant =>
      val json = Json.toJson(restaurant)
      Ok(json)
    }
  }

  def save = Action.async(parse.json) { implicit request =>
    val restaurant = request.body.validate[Restaurant]
    val received = restaurant.get
    val cRestaurant = CompleteRestaurant(0, received.name, received.address, received.city, 0, received.phone)
    print("Hola")
    Restaurants.save(cRestaurant) map { res =>
      if (res == "Restaurant saved") {
        saveTables(received.name, received.address)
      }
      Ok(res)
    }
  }
  def saveTables(name: String, address: String) = {
    Restaurants.getByNameAndAddress(name, address) map { res =>
      val r = res.get
      for (i <- 0 to 3) {
        TableRestaurants.save(TableRestaurant(0, r.restaurant_id, 4, true))
      }
    }
  }
}