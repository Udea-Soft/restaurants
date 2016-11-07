package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc.Controller
import play.api.libs.json.JsPath
import play.api.libs.functional.syntax._
import play.api.mvc.Action
import dao.CityDAO
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import models.Reservation
import play.api.libs.json.Reads
import dao.ReservationDAO
import java.sql.Timestamp
import java.util.Date
import models.CompleteReservation
import dao.TableRestaurants
import models.TableRestaurant

@Singleton
class ReservationController @Inject extends Controller {
  implicit val readReservation: Reads[Reservation] = (
    (JsPath \ "client").read[String] and
    (JsPath \ "restaurant_id").read[Int] and
    (JsPath \ "amount_people").read[Int])(Reservation.apply _)
  def createReservation = Action.async(parse.json) { implicit request =>
    val reservation = request.body.validate[Reservation]
    val received = reservation.get
    TableRestaurants.list(received.restaurant_id) map { r =>
      if (r == None) {
        Ok("No se puede reservar")
      } else {
        val id = r.get.table_restaurant_id
        val reserva = CompleteReservation(0, received.client, id, new Timestamp(new Date().getTime), 60, received.amount_people)
        ReservationDAO.save(reserva)
        Ok("reserva exitosa")
      }
    }

  }
}