package controllers

import javax.inject.Singleton
import javax.inject.Inject
import dao.OrderDAO
import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class OrderController @Inject()(orderDAO: OrderDAO) extends Controller{
  def getByReservation(reservation: Long) = Action.async {
    orderDAO.getByReservation(reservation) map { order =>
      val json = Json.toJson(order)
      Ok(json)
    }
  }
  
}