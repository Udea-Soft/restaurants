package dao

import javax.inject.Inject
import play.api.db.slick.HasDatabaseConfigProvider
import models._
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.Future

class CityDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val city = TableQuery[Cities]

  def all(): Future[Seq[City]] = db.run(city.result)

  def get(id: Long): Future[Option[City]] = db.run(city.filter(_.id_city===id).result.headOption)

  private class Cities(tag: Tag) extends Table[City](tag, "city") {
    def id_city = column[Long]("id_city",O.PrimaryKey,O.AutoInc)
    def name_city = column[String]("name_city")
    override def * = (id_city.?, name_city) <> ((City.apply _).tupled, City.unapply)
  }
}