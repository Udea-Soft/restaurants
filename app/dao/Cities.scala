package dao
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import model.City
import model.CityTableDef

object Cities {
  val dbConfig=DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val cities=TableQuery[CityTableDef]
  def list:Future[Seq[City]]={
    dbConfig.db.run(cities.result)
  }  
}