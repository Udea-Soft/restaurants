package model
import slick.driver.PostgresDriver.api._
case class TableRestaurant(table_restaurant_id: Long,restaurant:Long,capacity:Long,available:Boolean)
class TableRestaurantDef(tag: Tag) extends Table[TableRestaurant](tag, "table_restaurant") {
  def table_restaurant_id = column[Long]("table_restaurant_id",O.PrimaryKey,O.AutoInc)
  def restaurant = column[Long]("restaurant")
  def capacity=column[Long]("capacity")
  def available=column[Boolean]("available")
  override def * = (table_restaurant_id,restaurant,capacity,available) <> (TableRestaurant.tupled, TableRestaurant.unapply)
}