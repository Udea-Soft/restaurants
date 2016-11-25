package dao

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import models.Order
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import models.User

@Singleton
class OrderDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
  private val orders=TableQuery[Orders]
  private val users=TableQuery[Users]
  def getByReservation(reservation:Long):Future[Seq[Order]]={
    db.run(orders.filter(_.reservation===reservation).result)
  }
  def delete(id:Long):Future[Int]={
    db.run(orders.filter(_.id_delivery_dish===id).delete)
  }
  def save(order:Order):Future[String]={
    db.run(orders += order).map(res => "Plato agregado a la reserva").recover {
      case ex: Exception => "No se pudo agregar el plato a la reserva"
    }
  }
  def update(order:Order):Future[Int]={
    db.run(orders.filter(_.id_delivery_dish===order.id_delivery_dish).update(order))
  }
  def addBalance(user:User):Future[Int]={
    db.run(updateBalance(user.id_user,user.balance))
  }
  def updateBalance(id:Long,balance:Long):DBIO[Int]={
    sqlu"update user_restaurant set balance=balance+$balance where id_user=$id"
  }
  private class Orders(tag:Tag) extends Table[Order](tag,"order_restaurant"){
    def id_delivery_dish=column[Long]("id_delivery_dish",O.PrimaryKey,O.AutoInc)
    def dish=column[Long]("dish")
    def amount=column[Long]("amount")
    def type_d=column[Long]("type")
    def delivery=column[Long]("delivery")
    def reservation=column[Long]("reservation")
    def price=column[Double]("price")
    override def * = (id_delivery_dish.?,dish,amount,type_d.?,delivery.?,reservation.?,price)<>((Order.apply _).tupled,Order.unapply)
  }
  private class Users(tag:Tag) extends Table[User](tag,"user_restaurant"){
    def id_user=column[Long]("id_user",O.PrimaryKey,O.AutoInc)
    def balance=column[Long]("balance")
    override def * =(id_user,balance)<>((User.apply _).tupled,User.unapply)
    
  }
  
  
}
