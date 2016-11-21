package controllers

import javax.inject.Inject
import dao.TableRestaurantDAO
import play.api.mvc.Controller
import javax.inject.Singleton
import play.api.mvc.Action
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import models.TableRestaurant

@Singleton
class TableRestaurantController @Inject()(tableRestaurantDAO: TableRestaurantDAO) extends Controller {
  def getByRestaurant(restaurant:Long)=Action.async{
    tableRestaurantDAO.list(restaurant) map{tables=>
      val json=Json.toJson(tables)
      Ok(json)
    }
  }
  def insert=Action.async(parse.json){implicit request=>
    val table=request.body.validate[TableRestaurant].get
    tableRestaurantDAO.save(table) map{r=>
      Ok(r)
    }
  }
}