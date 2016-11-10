package controllers

import javax.inject.Singleton
import javax.inject.Inject

import play.api.mvc.Controller
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global
import models.Reservation
import dao.ReservationDAO
import dao.TableRestaurantDAO
import play.api.libs.json.Json

@Singleton
class ReservationController @Inject()(reservationDAO: ReservationDAO, tableRestaurantDAO: TableRestaurantDAO) extends Controller{

  def getByUser(id_user: Long) = Action.async {
      reservationDAO.getByUser(id_user) map { reservation =>
      val json = Json.toJson(reservation)
      Ok(json)
    }
  }
/*
  def insert = Action.async(parse.json) { implicit request =>
    val received = request.body.validate[Reservation].get
    tableRestaurantDAO.list(received.table_restaurant) map { r =>
      if (r == None) {
        Ok("No se puede reservar")
      } else {
        val id = r.get.id_table_restaurant
        val reserva = Reservation(None, received.user_restaurant, id, received.date_init, received.date_end, received.amount_people, received.state)
        reservationDAO.save(reserva)
        Ok("reserva exitosa")
      }
    }

  }*/
}