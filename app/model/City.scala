package model
import slick.driver.PostgresDriver.api._

case class City(city_id: Int, name: String)
class CityTableDef(tag: Tag) extends Table[City](tag, "city") {
  def city_id = column[Int]("city_id",O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  override def * = (city_id, name) <> (City.tupled, City.unapply)
}