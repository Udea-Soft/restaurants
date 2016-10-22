package model
import slick.driver.PostgresDriver.api._

case class Restaurant(name:String,address:String,city:Int,phone:String)
case class CompleteRestaurant(restaurant_id:Long,name:String,address:String,city:Int,score:Double,phone:String)
class RestaurantTableDef(tag:Tag) extends Table[CompleteRestaurant](tag,"restaurant"){
  def restaurant_id=column[Long]("restaurant_id",O.PrimaryKey,O.AutoInc)
  def name=column[String]("name")
  def address=column[String]("address")
  def city=column[Int]("city")
  def score=column[Double]("score")
  def phone=column[String]("phone")
  override def * = (restaurant_id,name,address,city,score,phone)<>(CompleteRestaurant.tupled,CompleteRestaurant.unapply)
}
