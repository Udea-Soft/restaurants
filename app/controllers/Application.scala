package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

import play.api.db._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Endpoints: "+
      "\n#Get all restaurants \nGET /restaurants \n#Get restaurant by id"+
      "\nGET /restaurants/:id \n#Save a restaurant \nPOST /restaurants "+
      "\n#Get all cities \nGET /cities \n#Create a reservation \nPOST /reservations"))
  }

  def db = Action {
    var out = ""
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())")

      val rs = stmt.executeQuery("SELECT tick FROM ticks")

      while (rs.next) {
        out += "Read from DB: " + rs.getTimestamp("tick") + "\n"
      }
    } finally {
      conn.close()
    }
    Ok(out)
  }
}
