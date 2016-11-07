package dao

import javax.inject.Inject
import play.api.db.slick.HasDatabaseConfigProvider
import models._
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RestaurantDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val restaurants = TableQuery[RestaurantTable]

  def all(): Future[Seq[Restaurant]] = db.run(restaurants.result)

  def get(id:Long): Future[Option[Restaurant]] =
    dbConfig.db.run(restaurants.filter(_.id_restaurant===id).result.headOption)

  def getByName(name:String): Future[Seq[Restaurant]]={
    dbConfig.db.run(restaurants.filter(_.name_restaurant.toLowerCase like ("%" + name.toLowerCase + "%")).result)
  }

  def save(restaurant: Restaurant):Future[String]={
    dbConfig.db.run(restaurants += restaurant).map(res => "Restaurant saved").recover{
      case ex: Exception => "Error no se pudo guardar el restaurante"
    }
  }

  class RestaurantTable(tag:Tag) extends Table[Restaurant](tag,"restaurant"){
    def id_restaurant=column[Long]("id_restaurant",O.PrimaryKey,O.AutoInc)
    def name_restaurant=column[String]("name_restaurant")
    def description=column[String]("description")
    def email=column[String]("email")
    def admin=column[Long]("admin")
    override def * = (id_restaurant,name_restaurant,description,email,admin)<>((Restaurant.apply _).tupled, Restaurant.unapply)
  }

}