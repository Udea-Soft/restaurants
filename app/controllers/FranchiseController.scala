package controllers

import javax.inject.{Inject, Singleton}

import dao.{FranchiseDAO, RestaurantDAO}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class FranchiseController @Inject()(franchiseDAO: FranchiseDAO) extends Controller {

  def getAll = Action.async {
    franchiseDAO.all map { franchise =>
      val json = Json.toJson(franchise)
      Ok(json)
    }
  }
}
