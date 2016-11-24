package controllers

import javax.inject.Singleton
import javax.inject.Inject
import dao.DishDAO
import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class DishController @Inject()(dishDAO: DishDAO) extends Controller{  
  def getDishesByRestaurant(restaurant:Long)=Action.async{
    dishDAO.getByRestaurant(restaurant) map{dish=>
      val json=Json.toJson(dish)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  def getById(id:Long)=Action.async{
    dishDAO.getByDish(id) map{dish=>
      val json=Json.toJson(dish)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
  
}