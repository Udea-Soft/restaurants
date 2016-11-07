package models

import slick.driver.PostgresDriver.api._
import java.sql.Date
import java.sql.Timestamp

case class Reservation(client: String, restaurant_id: Int,amount_people: Int)
case class CompleteReservation(reservation_id: Long, client: String, table_restaurant: Long, reservation_date: Timestamp, reservation_duration: Int, amount_people: Int)
class ReservationTableDef(tag: Tag) extends Table[CompleteReservation](tag, "reservation") {
  def reservation_id = column[Long]("reservation_id",O.PrimaryKey,O.AutoInc)
  def client = column[String]("client")
  def table_restaurant = column[Long]("table_restaurant")
  def reservation_date = column[Timestamp]("reservation_date")
  def reservation_duration = column[Int]("reservation_duration")
  def amount_people = column[Int]("amount_people")
  override def * = (reservation_id, client, table_restaurant, reservation_date, reservation_duration, amount_people) <> (CompleteReservation.tupled, CompleteReservation.unapply)
}
