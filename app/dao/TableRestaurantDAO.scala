package dao

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import models.TableRestaurant

class TableRestaurantDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val tables = TableQuery[TableRestaurants]

  def list(restaurant: Long): Future[Seq[TableRestaurant]] = {
    db.run(tables.filter(_.franchise === restaurant).result)
  }

  def save(table: TableRestaurant): Future[String] = {
    db.run(tables += table).map(res => "Table saved").recover {
      case ex: Exception => "No se pudo guardar la mesa"
    }
  }

  def update(table: TableRestaurant): Future[Int] = {
    db.run(tables.filter(_.id_table_restaurant === table.id_table_restaurant).update(table))
  }

  private class TableRestaurants(tag: Tag) extends Table[TableRestaurant](tag, "table_restaurant") {
    def id_table_restaurant = column[Long]("id_table_restaurant", O.PrimaryKey, O.AutoInc)

    def franchise = column[Long]("franchise")

    def capacity = column[Long]("capacity")

    def available = column[Boolean]("available")

    override def * = (id_table_restaurant.?, franchise, capacity, available) <> ((TableRestaurant.apply _).tupled, TableRestaurant.unapply)
  }

}