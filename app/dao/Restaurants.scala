package dao

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play
import slick.driver.PostgresDriver.api._
import model.RestaurantTableDef
import scala.concurrent.Future
import model.CompleteRestaurant
import scala.concurrent.ExecutionContext.Implicits.global

object Restaurants {
  val dbConfig=DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val restaurants=TableQuery[RestaurantTableDef]
  def list:Future[Seq[CompleteRestaurant]]={
    dbConfig.db.run(restaurants.result)
  }
  def get(id:Long): Future[Option[CompleteRestaurant]]={
    dbConfig.db.run(restaurants.filter(_.restaurant_id===id).result.headOption)
  }  
  
  def save(restaurant:CompleteRestaurant):Future[String]={
    dbConfig.db.run(restaurants+=restaurant).map(res => "Restaurant saved").recover{
      case ex: Exception => "Error"
    }
  }
  def getByNameAndAddress(name:String,address:String): Future[Option[CompleteRestaurant]]={
    dbConfig.db.run(restaurants.filter(_.name===name).filter(_.address===address).result.headOption)
  }  
}