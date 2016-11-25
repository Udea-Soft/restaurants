package dao

import slick.driver.PostgresDriver.api._
import models.UserRestaurant

class UsersRestaurant(tag: Tag) extends Table[UserRestaurant](tag, "user_restaurant") {
  def id_user = column[Long]("id_user", O.PrimaryKey, O.AutoInc)

  def username = column[String]("username")

  def name = column[String]("name")

  override def * = (id_user, username, name) <> ((UserRestaurant.apply _).tupled, UserRestaurant.unapply)
}