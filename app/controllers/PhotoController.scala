package controllers

import javax.inject.Inject
import dao.PhotoDAO
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import javax.inject.Singleton
import play.api.mvc.Controller
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PhotoController @Inject()(photoDAO: PhotoDAO)extends Controller{

  def getByFranchise(id_franchise: Long) = Action.async {
    photoDAO.getByRestaurant(id_franchise) map { photo =>
      val json = Json.toJson(photo)
      Ok(json)
    }
  }
}
