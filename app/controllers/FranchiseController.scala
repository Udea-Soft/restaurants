package controllers

import javax.inject.{Inject, Singleton}

import dao.FranchiseDAO
import models.Franchise
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class FranchiseController @Inject()(franchiseDAO: FranchiseDAO) extends Controller {

  def getAll = Action.async {
    franchiseDAO.all map { franchise =>
      val json = Json.toJson(franchise)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }

  def getById(id: Long) = Action.async {
    franchiseDAO.get(id) map { franchise =>
      val json = Json.toJson(franchise)
      Ok(json).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }

  def insert = Action.async(parse.json) { implicit request =>

    val received = request.body.validate[Franchise].get
    val receivedFranchise = Franchise(None, received.name_franchise, received.restaurant, received.address, received.city, received.phone, received.latitude, received.longitude, received.open_time_week, received.close_time_week, received.open_time_weekend, received.close_time_weekend)
    franchiseDAO.save(receivedFranchise) map { res =>
      Ok(res).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
}
