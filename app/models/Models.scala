package models

import java.sql.{ Time, Timestamp }
import java.text.SimpleDateFormat
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

case class Reservation(id_reservation: Option[Long], user_restaurant: Long, table_restaurant: Long,
                       date_init: Timestamp, date_end: Timestamp, amount_people: Int,
                       state: Int)
case class CompleteReservation(reservation:ReservationTable, franchise: Franchise)

object Reservation {
  implicit object TimestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")

    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }

    def writes(ts: Timestamp) = JsString(format.format(ts))
  }
  implicit val reservationWrites = Json.writes[Reservation]
  implicit val reservationReads = Json.reads[Reservation]
}

case class Photo(id_photo: Option[Long], url_photo: String, restaurant: Long)

object Photo {
  implicit val photoWrites = Json.writes[Photo]
  implicit val photoReads = Json.reads[Photo]
}


case class TableRestaurant(id_table_restaurant: Option[Long], franchise: Long, capacity: Long, available: Boolean)
object TableRestaurant {
  implicit val photoWrites = Json.writes[TableRestaurant]
  implicit val photoReads = Json.reads[TableRestaurant]
}
case class ReservationTable(reservation:Reservation,table:TableRestaurant)



// cambiomenudespuesdepago reservasperiodicas repotereservaspagadas
//Menu
case class Order(id_delivery_dish: Option[Long], dish: Long, amount: Long, type_d: Option[Long], delivery: Option[Long], reservation: Option[Long], price: Double)

object Order {
  implicit val orderWrites = Json.writes[Order]
  implicit val orderReads = Json.reads[Order]
}

case class Dish(id_dish: Long, name_dish: String, description: String, price: Double, restaurant: Long, type_d: Long)

object Dish {
  implicit val dishWrites = Json.writes[Dish]
  implicit val dishReads = Json.reads[Dish]
}

case class DishType(id_dish_type: Long, type_d: String)

object DishType {
  implicit val dishTypeWrites = Json.writes[DishType]
  implicit val dishTypeReads = Json.reads[DishType]
}
case class CompleteDish(dish: Dish, type_d: DishType)

object CompleteDish {
  implicit val dishWrites = Json.writes[CompleteDish]
  implicit val dishReads = Json.reads[CompleteDish]
}

case class Payment(id_payment:Option[Long],reservation:Long,state:Long)

object Payment{
  implicit val paymentWrites=Json.writes[Payment]
  implicit val paymentReads=Json.reads[Payment]
}
case class PaymentReservation(reservation:Reservation,payment:Payment)
object PaymentReservation{
  implicit val paymentWrites=Json.writes[PaymentReservation]
  implicit val paymentReads=Json.reads[PaymentReservation]
}
object ReservationTable {
  implicit val photoWrites = Json.writes[ReservationTable]
  implicit val photoReads = Json.reads[ReservationTable]
}
object CompleteReservation {
  implicit val photoWrites = Json.writes[CompleteReservation]
  implicit val photoReads = Json.reads[CompleteReservation]
}
case class OrderID(id_delivery_dish:Long)

object OrderID{
  implicit val orderWrites=Json.writes[OrderID]
  implicit val orderReads=Json.reads[OrderID]
}

case class User(id_user:Long,balance:Long)

object User{
  implicit val userWrites=Json.writes[User]
  implicit val userReads=Json.reads[User]
}

case class UserRestaurant(id_user:Long,username:String,name:String)

object UserRestaurant{
  implicit val userWrites=Json.writes[UserRestaurant]
  implicit val userReads=Json.reads[UserRestaurant]
}
case class ReportReservation(reservation:Reservation,table:TableRestaurant,user:UserRestaurant,franchise:Franchise,payment:Payment)

object ReportReservation{
  implicit val reportWrites=Json.writes[ReportReservation]
  implicit val reportReads=Json.reads[ReportReservation]
}