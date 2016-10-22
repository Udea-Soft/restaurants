package dao
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import model.ReservationTableDef
import scala.concurrent.ExecutionContext.Implicits.global
import model.CompleteReservation

object Reservations {
  val dbConfig=DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val reservations=TableQuery[ReservationTableDef]  
  def save(reservation:CompleteReservation):Future[String]={
    dbConfig.db.run(reservations+=reservation).map(res => "Restaurant saved").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }
}