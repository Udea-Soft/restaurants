package dao

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import models.Dish
import slick.driver.PostgresDriver.api._
import models.DishType
import models.CompleteDish
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DishDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  private val dishes = TableQuery[Dishes]
  private val dishTypes = TableQuery[DishTypes]
  val tupledJoin: Query[(Dishes, DishTypes), (Dish, DishType), Seq] = dishes join dishTypes on (_.type_d === _.id_dish_type)
  val joinAction: DBIO[Seq[(Dish, DishType)]] = tupledJoin.result

  def getByRestaurant(restaurant: Long): Future[Seq[CompleteDish]] = {
    get map { dish =>
      dish.filter { d => d.dish.restaurant == restaurant }
    }
  }

  def getByDish(id: Long): Future[Option[CompleteDish]] = {
    get map { dish =>
      dish.filter { d => d.dish.id_dish == id }.headOption
    }
  }

  private def get: Future[Seq[CompleteDish]] = {
    db.run(joinAction) map { dish =>
      dish map (d => (CompleteDish.apply _) tupled d): Seq[CompleteDish]
    }
  }

  class Dishes(tag: Tag) extends Table[Dish](tag, "dish") {
    def id_dish = column[Long]("id_dish", O.PrimaryKey, O.AutoInc)

    def name_dish = column[String]("name_dish")

    def description = column[String]("description")

    def price = column[Double]("price")

    def restaurant = column[Long]("restaurant")

    def type_d = column[Long]("type")

    override def * = (id_dish, name_dish, description, price, restaurant, type_d) <> ((Dish.apply _).tupled, Dish.unapply)
  }

  class DishTypes(tag: Tag) extends Table[DishType](tag, "dish_type") {
    def id_dish_type = column[Long]("id_dish_type", O.PrimaryKey, O.AutoInc)

    def type_d = column[String]("type")

    override def * = (id_dish_type, type_d) <> ((DishType.apply _).tupled, DishType.unapply)
  }

}