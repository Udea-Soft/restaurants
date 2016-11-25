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
import models.CompleteReservation
import java.sql.Timestamp
import java.text.SimpleDateFormat

@Singleton
class ReservationController @Inject()(reservationDAO: ReservationDAO, tableRestaurantDAO: TableRestaurantDAO) extends Controller{

  def getByUser(id_user: Long) = Action.async {
      reservationDAO.getByUser(id_user) map { reservation =>      
      val json = Json.toJson(reservation)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
   def getByUserWithPayment(id_user: Long) = Action.async {
      reservationDAO.getByUserWithPayment(id_user) map { reservation =>      
      val json = Json.toJson(reservation)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def getAll = Action.async {
      reservationDAO.all() map { reservation =>
      val json = Json.toJson(reservation)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def getByRange(franchise:Long,start:String,end:String) = Action.async {
      val format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
      reservationDAO.getByRange(franchise,new Timestamp(format.parse(start).getTime),new Timestamp(format.parse(end).getTime)) map { reservation =>
      val json = Json.toJson(reservation)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def insert=Action.async(parse.json){implicit request=>
    val received=request.body.validate[Reservation].get
    reservationDAO.save(received) map{r=>
      Ok(r).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def getPaid(start:String,end:String)=Action.async{
    val format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
    reservationDAO.getWithUser(new Timestamp(format.parse(start).getTime),new Timestamp(format.parse(end).getTime)) map{r=>
      val json=Json.toJson(r)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
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