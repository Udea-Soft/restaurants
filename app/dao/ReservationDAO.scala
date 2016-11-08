package dao
import java.sql.Timestamp
import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future
import models.Reservation

import scala.concurrent.ExecutionContext.Implicits.global
class ReservationDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val reservations = TableQuery[Reservations]

  def all(): Future[Seq[Reservation]] = db.run(reservations.result)

  def getByUser(id_user: Long):Future[Seq[Reservation]] = {
    db.run(reservations.filter(_.user_restaurant === id_user).result)
  }

  def save(reservation:Reservation):Future[String]={
    db.run(reservations+=reservation).map(res => "Reserva guardada").recover{
      case ex: Exception => "Error no se pudo guardar la reserva\n" + ex.getCause.getMessage
    }
  }
  class Reservations(tag: Tag) extends Table[Reservation](tag, "reservation") {
    def id_reservation = column[Long]("id_reservation",O.PrimaryKey,O.AutoInc)
    def user_restaurant = column[Long]("user_restaurant")
    def table_restaurant = column[Long]("table_restaurant")
    def date_init = column[Timestamp]("date_init")
    def date_end = column[Timestamp]("date_end")
    def amount_people = column[Int]("amount_people")
    def state = column[Int]("state")
    override def * = (id_reservation, user_restaurant, table_restaurant, date_init, date_end,
      amount_people, state) <> ((Reservation.apply _).tupled, Reservation.unapply)
  }
}