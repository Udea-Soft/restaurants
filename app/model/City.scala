package model
import slick.driver.PostgresDriver.api._

case class City(city_id: Int, name_city: String)
class CityTableDef(tag: Tag) extends Table[City](tag, "city") {
  def city_id = column[Int]("city_id",O.PrimaryKey,O.AutoInc)
  def name_city = column[String]("name_city")
  override def * = (city_id, name_city) <> (City.tupled, City.unapply)
}