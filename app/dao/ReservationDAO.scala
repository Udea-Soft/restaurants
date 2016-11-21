package dao

import java.sql.Timestamp
import java.util.Calendar
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import models.Reservation
import scala.concurrent.ExecutionContext.Implicits.global
import models.TableRestaurant
import models.CompleteReservation
import java.util.Calendar
import models.Payment
import models.PaymentReservation


class ReservationDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val reservations = TableQuery[Reservations]
  val tables_restaurant=TableQuery[TablesRestaurant];
  val payments=TableQuery[Payments]
  val paymentJoin:Query[(Reservations,Payments),(Reservation,Payment), Seq] = reservations join payments on (_.id_reservation ===_.reservation)
  val paymentAction:DBIO[Seq[(Reservation,Payment)]]=paymentJoin.result
  val tupledJoin: Query[(Reservations,TablesRestaurant),(Reservation,TableRestaurant), Seq] = reservations join tables_restaurant on (_.table_restaurant ===_.id_table_restaurant)
  val joinAction:DBIO[Seq[(Reservation,TableRestaurant)]]=tupledJoin.result
  def all(): Future[Seq[CompleteReservation]] = get
  
  def getByUserWithPayment(user:Long):Future[Seq[PaymentReservation]]={
    getWithPayment map{payment=>
      payment filter (_.reservation.user_restaurant==user)
    }
  }
  private def getWithPayment:Future[Seq[PaymentReservation]]={
    db.run(paymentAction) map{payment=>
      payment map(p=>(PaymentReservation.apply _) tupled p):Seq[PaymentReservation]      
    }
  }
  def getByUser(user:Long):Future[Seq[CompleteReservation]] = {
     get map{reservation=>
       reservation.filter(_.reservation.user_restaurant==user) 
     }
  }
  def getByRange(franchise:Long,start:Timestamp,end:Timestamp):Future[Seq[CompleteReservation]]={
    get map{reservation=>
      reservation.filter {r => r.table_restaurant.franchise==franchise&&inRange(start,r.reservation.date_init,r.reservation.date_end,end)}  
    }
  }
  private def inRange(start:Timestamp,date:Timestamp,date2:Timestamp,end:Timestamp):Boolean={
    if(after(date,start)&&before(date,end)&&between(start,date,date2,end))true;
    else false;
  }
  private def after(date:Timestamp,date2:Timestamp):Boolean={
    if(date.getTime()>=date2.getTime())true;
    else false;
  }
  private def before(date:Timestamp,date2:Timestamp):Boolean={
    if(date.getTime()<=date2.getTime())true;
    else false;
  }
  private def between(start:Timestamp,date:Timestamp,date2:Timestamp,end:Timestamp):Boolean={
    val startC:Calendar=Calendar.getInstance();
    val dateC:Calendar=Calendar.getInstance();
    val endC:Calendar=Calendar.getInstance();
    val date2C:Calendar=Calendar.getInstance();
    startC.setTimeInMillis(start.getTime());
    dateC.setTimeInMillis(date.getTime());
    date2C.setTimeInMillis(date2.getTime());
    endC.setTimeInMillis(end.getTime());
    if(dateC.get(Calendar.HOUR_OF_DAY)<startC.get(Calendar.HOUR_OF_DAY)&&date2C.get(Calendar.HOUR_OF_DAY)<=startC.get(Calendar.HOUR_OF_DAY)){   
      false;      
    }
    if(dateC.get(Calendar.HOUR_OF_DAY)>endC.get(Calendar.HOUR_OF_DAY)){
        false;
    }
    if(dateC.get(Calendar.HOUR_OF_DAY)==endC.get(Calendar.HOUR_OF_DAY)&&dateC.get(Calendar.MINUTE)>endC.get(Calendar.MINUTE)){
        false;
    }
    else true;
  }
  private def get:Future[Seq[CompleteReservation]]={
    db.run(joinAction) map{ reservation=> 
      reservation  map(r=>(CompleteReservation.apply _) tupled r):Seq[CompleteReservation]
    }
  }
  def save(reservation:Reservation):Future[String]={
    db.run(reservations+=reservation).map(res => "Reserva guardada").recover{
      case ex: Exception => "Error no se pudo guardar la reserva\n" + ex.getCause.getMessage
    }
  }
  
  class Reservations(tag: Tag) extends Table[Reservation](tag, "reservation") {
    def id_reservation = column[Long]("id_reservation",O.PrimaryKey,O.AutoInc)
    def user_restaurant = column[Long]("user_restaurant")
    def table_restaurant = column[Long]("table_restaurant")
    def date_init = column[Timestamp]("date_init")
    def date_end = column[Timestamp]("date_end")
    def amount_people = column[Int]("amount_people")
    def state = column[Int]("state")
    override def * = (id_reservation.?, user_restaurant, table_restaurant, date_init, date_end,
      amount_people, state) <> ((Reservation.apply _).tupled, Reservation.unapply)
  }
  class Payments(tag:Tag) extends Table[Payment](tag,"payment"){
    def id_payment=column[Long]("id_payment")
    def reservation=column[Long]("reservation")
    def state=column[Long]("state")
    override def * = (id_payment.?,reservation,state)<>((Payment.apply _).tupled,Payment.unapply)
  }
}

class TablesRestaurant(tag:Tag) extends Table[TableRestaurant](tag,"table_restaurant"){
  def id_table_restaurant=column[Long]("id_table_restaurant");
  def franchise=column[Long]("franchise");
  def capacity=column[Long]("capacity");
  def available=column[Boolean]("available");
  override def * = (id_table_restaurant.?,franchise,capacity,available)<>((TableRestaurant.apply _).tupled,TableRestaurant.unapply)
}