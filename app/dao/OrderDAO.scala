package dao

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import models.Order
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import javax.inject.Singleton

@Singleton
class OrderDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
  private val orders=TableQuery[Orders]
  def getByReservation(reservation:Long):Future[Seq[Order]]={
    db.run(orders.filter(_.reservation===reservation).result)
  }
  private class Orders(tag:Tag) extends Table[Order](tag,"order_restaurant"){
    def id_delivery_dish=column[Long]("id_delivery_dish",O.PrimaryKey,O.AutoInc)
    def dish=column[Long]("dish")
    def amount=column[Long]("amount")
    def type_d=column[Long]("type")
    def delivery=column[Long]("delivery")
    def reservation=column[Long]("reservation")
    def price=column[Double]("price")
    override def * = (id_delivery_dish.?,dish,amount,type_d,delivery.?,reservation.?,price)<>((Order.apply _).tupled,Order.unapply)
  }
  
}
