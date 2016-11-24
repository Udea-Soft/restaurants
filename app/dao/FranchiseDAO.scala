package dao

import javax.inject.Inject
import play.api.db.slick.HasDatabaseConfigProvider
import models.Franchise
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import java.sql.Time
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class FranchiseDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val franchises = TableQuery[Franchises]

  def all(): Future[Seq[Franchise]] = db.run(franchises.result)

  def get(id: Long): Future[Option[Franchise]] =
    db.run(franchises.filter(_.id_franchise === id).result.headOption)


  def save(franchise: Franchise): Future[String] = {
    db.run(franchises += franchise).map(res => "Franchise saved").recover {
      case ex: Exception => "Error no se pudo guardar la franquicia\n" + ex.getCause.getMessage
    }
  }
}
class Franchises(tag: Tag) extends Table[Franchise](tag, "franchise") {

    def id_franchise = column[Long]("id_franchise", O.PrimaryKey, O.AutoInc)

    def name_franchise = column[String]("name_franchise")

    def restaurant = column[Long]("restaurant")

    def address = column[String]("address")

    def city = column[Long]("city")

    def phone = column[String]("phone")

    def latitude = column[Double]("latitude")

    def longitude = column[Double]("longitude")

    def open_time_week = column[Time]("open_time_week")

    def close_time_week = column[Time]("close_time_week")

    def open_time_weekend = column[Time]("open_time_weekend")

    def close_time_weekend = column[Time]("close_time_weekend")

    override def * = (id_franchise.?, name_franchise, restaurant, address, city, phone, latitude, longitude,
      open_time_week, close_time_week, open_time_weekend, close_time_weekend) <> ((Franchise.apply _).tupled, Franchise.unapply)
  }