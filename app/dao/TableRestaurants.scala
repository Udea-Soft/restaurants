package dao
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import models.TableRestaurantDef
import models.TableRestaurant

object TableRestaurants {
  val dbConfig=DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val tables=TableQuery[TableRestaurantDef]
  def list(restaurant:Long):Future[Option[TableRestaurant]]={
    dbConfig.db.run(tables.filter(_.restaurant===restaurant).result.headOption)
  }  
  def save(table:TableRestaurant):Future[String]={
    dbConfig.db.run(tables+=table).map(res => "Table saved").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }
  def update(table:TableRestaurant):Future[Int]={
    dbConfig.db.run(tables.filter(_.table_restaurant_id===table.table_restaurant_id).update(table)) 
  }
}