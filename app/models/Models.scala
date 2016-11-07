package models

import play.api.libs.json.Json


case class City(id_city: Long, name_city: String)
object City {
  implicit val cityWrites = Json.writes[City]
}

case class Restaurant(id_restaurant: Long, name_restaurant:String, description:String, email:String, admin:Long)
object Restaurant {
  implicit val restaurantWrites = Json.writes[Restaurant]
}