package dao

import models.Photo
import javax.inject.Inject

import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PhotoDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val photos = TableQuery[Photos]

  def getByRestaurant(id_restaurant: Long): Future[Seq[Photo]] = {
    db.run(photos.filter(_.restaurant === id_restaurant).result)
  }

  def save(photo: Photo): Future[String] = {
    db.run(photos += photo).map(res => "Photo saved").recover {
      case ex: Exception => "Error no se pudo guardar la foto\n" + ex.getCause.getMessage
    }
  }

  private class Photos(tag: Tag) extends Table[Photo](tag, "photo") {
    def id_photo = column[Long]("id_photo", O.PrimaryKey, O.AutoInc)

    def url_photo = column[String]("url_photo")

    def restaurant = column[Long]("restaurant")

    override def * = (id_photo.?, url_photo, restaurant) <> ((Photo.apply _).tupled, Photo.unapply)
  }

}
