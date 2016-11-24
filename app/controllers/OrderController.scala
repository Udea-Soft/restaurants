package controllers

import javax.inject.Singleton
import javax.inject.Inject
import dao.OrderDAO
import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import models.OrderID
import models.Order
import models.User

@Singleton
class OrderController @Inject()(orderDAO: OrderDAO) extends Controller{
  def getByReservation(reservation: Long) = Action.async {
    orderDAO.getByReservation(reservation) map { order =>
      val json = Json.toJson(order)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def delete=Action.async(parse.json){implicit request=>
    val received=request.body.validate[OrderID].get
    orderDAO.delete(received.id_delivery_dish) map{count=>
      var message:String=""
      if(count==0){
        message="No se pudo eliminar el plato"
      }
      else{
        message="Plato eliminado de la reserva"
      }
      Ok(message).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def save=Action.async(parse.json){implicit request=>
    var received:Order=request.body.validate[Order].get    
    var newDish=new Order(received.id_delivery_dish,received.dish,received.amount,Some(1),received.delivery,received.reservation,received.price)
    orderDAO.save(newDish) map{response=>
      Ok(response).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def update=Action.async(parse.json){implicit request=>
    var received=request.body.validate[Order].get
    orderDAO.update(received) map{count=>
      var message=""
      if(count==0){
        message="No se pudo modificar el plato de la reserva"
      }
      else{
        message="Plato actualizado en la reserva"
      }
      Ok(message).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def updateBalance=Action.async(parse.json){implicit request=>
    val received=request.body.validate[User].get
    orderDAO.addBalance(received) map{count=>
      var message=""
      if(count==0){
        message="No se pudo agregar el saldo"
      }else{
        message="Se agregó el saldo al usuario"
      }
      Ok(message).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
     }
  }
}