package dao

import java.sql.Timestamp
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
import models.Franchise
import models.ReservationTable
import models.UserRestaurant
import models.ReportReservation


class ReservationDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val reservations = TableQuery[Reservations]
  val tables_restaurant = TableQuery[TablesRestaurant];
  val franchises = TableQuery[Franchises]
  val payments = TableQuery[Payments]
  val users = TableQuery[UsersRestaurant]
  val paymentJoin: Query[(Reservations, Payments), (Reservation, Payment), Seq] = reservations join payments on (_.id_reservation === _.reservation)
  val paymentAction: DBIO[Seq[(Reservation, Payment)]] = paymentJoin.result
  val tupledJoin: Query[((Reservations, TablesRestaurant), Franchises), ((Reservation, TableRestaurant), Franchise), Seq] = reservations join tables_restaurant on (_.table_restaurant === _.id_table_restaurant) join franchises on (_._2.franchise === _.id_franchise)
  val joinAction: DBIO[Seq[((Reservation, TableRestaurant), Franchise)]] = tupledJoin.result
  val emberJoin: Query[((((Reservations, TablesRestaurant), Franchises), UsersRestaurant), Payments), ((((Reservation, TableRestaurant), Franchise), UserRestaurant), Payment), Seq] = reservations join tables_restaurant on (_.table_restaurant === _.id_table_restaurant) join franchises on (_._2.franchise === _.id_franchise) join users on (_._1._1.user_restaurant === _.id_user) join payments on (_._1._1._1.id_reservation === _.reservation)
  val emberAction: DBIO[Seq[((((Reservation, TableRestaurant), Franchise), UserRestaurant), Payment)]] = emberJoin.result

  def all(): Future[Seq[CompleteReservation]] = get

  def getByUserWithPayment(user: Long): Future[Seq[PaymentReservation]] = {
    getWithPayment map { payment =>
      payment filter (_.reservation.user_restaurant == user)
    }
  }

  def getWithUser(start: Timestamp, end: Timestamp): Future[Seq[ReportReservation]] = {
    getEmber map { reservations =>
      reservations filter (r => r.payment.state == 1 && after(r.reservation.date_init, start) && before(r.reservation.date_end, end))
    }
  }

  private def getWithPayment: Future[Seq[PaymentReservation]] = {
    db.run(paymentAction) map { payment =>
      payment map (p => (PaymentReservation.apply _) tupled p): Seq[PaymentReservation]
    }
  }

  private def getEmber: Future[Seq[ReportReservation]] = {
    db.run(emberAction) map { reserv =>
      reserv map { r =>
        var reservatio = r._1._1._1._1
        var table = r._1._1._1._2
        var franchise = r._1._1._2
        var user = r._1._2
        var payment = r._2
        (ReportReservation.apply _) tupled(reservatio, table, user, franchise, payment)
      }: Seq[ReportReservation]
    }
  }

  def getByUser(user: Long): Future[Seq[CompleteReservation]] = {
    get map { reservation =>
      reservation.filter(_.reservation.reservation.user_restaurant == user)
    }
  }

  def getByRange(franchise: Long, start: Timestamp, end: Timestamp): Future[Seq[CompleteReservation]] = {
    get map { reservation =>
      reservation.filter { r => r.reservation.table.franchise == franchise && inRange(start, r.reservation.reservation.date_init, r.reservation.reservation.date_end, end) }
    }
  }

  private def inRange(start: Timestamp, date: Timestamp, date2: Timestamp, end: Timestamp): Boolean = {
    if (after(date, start) && before(date, end) && between(start, date, date2, end)) true;
    else false;
  }

  private def after(date: Timestamp, date2: Timestamp): Boolean = {
    if (date.getTime() >= date2.getTime()) true;
    else false;
  }

  private def before(date: Timestamp, date2: Timestamp): Boolean = {
    if (date.getTime() <= date2.getTime()) true;
    else false;
  }

  private def between(start: Timestamp, date: Timestamp, date2: Timestamp, end: Timestamp): Boolean = {
    val startC: Calendar = Calendar.getInstance();
    val dateC: Calendar = Calendar.getInstance();
    val endC: Calendar = Calendar.getInstance();
    val date2C: Calendar = Calendar.getInstance();
    startC.setTimeInMillis(start.getTime());
    dateC.setTimeInMillis(date.getTime());
    date2C.setTimeInMillis(date2.getTime());
    endC.setTimeInMillis(end.getTime());
    if (dateC.get(Calendar.HOUR_OF_DAY) < startC.get(Calendar.HOUR_OF_DAY) && date2C.get(Calendar.HOUR_OF_DAY) <= startC.get(Calendar.HOUR_OF_DAY)) {
      false;
    }
    if (dateC.get(Calendar.HOUR_OF_DAY) > endC.get(Calendar.HOUR_OF_DAY)) {
      false;
    }
    if (dateC.get(Calendar.HOUR_OF_DAY) == endC.get(Calendar.HOUR_OF_DAY) && dateC.get(Calendar.MINUTE) > endC.get(Calendar.MINUTE)) {
      false;
    }
    else true;
  }

  private def get: Future[Seq[CompleteReservation]] = {
    db.run(joinAction) map { reservation =>
      reservation map { r =>
        val reservation = (ReservationTable.apply _) tupled r._1
        (CompleteReservation.apply _) tupled(reservation, r._2)
      }: Seq[CompleteReservation]
    }
  }

  def save(reservation: Reservation): Future[String] = {
    db.run(reservations += reservation).map(res => "Reserva guardada").recover {
      case ex: Exception => "Error no se pudo guardar la reserva"
    }
  }

  class Reservations(tag: Tag) extends Table[Reservation](tag, "reservation") {
    def id_reservation = column[Long]("id_reservation", O.PrimaryKey, O.AutoInc)

    def user_restaurant = column[Long]("user_restaurant")

    def table_restaurant = column[Long]("table_restaurant")

    def date_init = column[Timestamp]("date_init")

    def date_end = column[Timestamp]("date_end")

    def amount_people = column[Int]("amount_people")

    def state = column[Int]("state")

    override def * = (id_reservation.?, user_restaurant, table_restaurant, date_init, date_end,
      amount_people, state) <> ((Reservation.apply _).tupled, Reservation.unapply)
  }

  class Payments(tag: Tag) extends Table[Payment](tag, "payment") {
    def id_payment = column[Long]("id_payment")

    def reservation = column[Long]("reservation")

    def state = column[Long]("state")

    override def * = (id_payment.?, reservation, state) <> ((Payment.apply _).tupled, Payment.unapply)
  }

}

class TablesRestaurant(tag: Tag) extends Table[TableRestaurant](tag, "table_restaurant") {
  def id_table_restaurant = column[Long]("id_table_restaurant");

  def franchise = column[Long]("franchise");

  def capacity = column[Long]("capacity");

  def available = column[Boolean]("available");

  override def * = (id_table_restaurant.?, franchise, capacity, available) <> ((TableRestaurant.apply _).tupled, TableRestaurant.unapply)
}