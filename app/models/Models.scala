package models

import java.sql.Time
import java.text.SimpleDateFormat

import org.joda.time.LocalTime
import play.api.libs.json._


case class City(id_city: Option[Long], name_city: String)

object City {
  implicit val cityWrites = Json.writes[City]
}

case class Restaurant(id_restaurant: Option[Long], name_restaurant: String, description: String, email: String, admin: Long)

object Restaurant {
  implicit val restaurantWrites = Json.writes[Restaurant]
  implicit val restaurantReads = Json.reads[Restaurant]
}

case class Franchise(id_franchise: Option[Long], name_franchise: String, restaurant: Long, address: String,
                     city: Long, phone: String, latitude: Double, longitude: Double,
                     open_time_week: Time, close_time_week: Time, open_time_weekend: Time,
                     close_time_weekend: Time)

object Franchise {

  implicit object TimeFormat extends Format[Time] {
    val format = new SimpleDateFormat("HH:mm")

    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Time(format.parse(str).getTime))
    }

    def writes(ts: Time) = JsString(format.format(ts))
  }

  implicit val franchiseWrites = Json.writes[Franchise]
  implicit val franchiseReads = Json.reads[Franchise]
}

