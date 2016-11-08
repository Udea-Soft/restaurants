package controllers

import javax.inject.Singleton
import javax.inject.Inject

import play.api.mvc.Controller
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import models.Reservation
import dao.ReservationDAO
import dao.TableRestaurants
import play.api.libs.json.Json

@Singleton
class ReservationController @Inject()(reservationDAO: ReservationDAO) extends Controller{

  def getByUser(id_user: Long) = Action.async {
      reservationDAO.getByUser(id_user) map { reservation =>
      val json = Json.toJson(reservation)
      Ok(json)
    }
  }

  def insert = Action.async(parse.json) { implicit request =>
    val received = request.body.validate[Reservation].get
    TableRestaurants.list(received.id_reservation) map { r =>
      if (r == None) {
        Ok("No se puede reservar")
      } else {
        val id = r.get.table_restaurant_id
        val reserva = Reservation(0, received.user_restaurant, id, received.date_init, received.date_end, received.amount_people, received.state)
        reservationDAO.save(reserva)
        Ok("reserva exitosa")
      }
    }

  }
}