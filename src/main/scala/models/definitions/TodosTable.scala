package models.definitions

import models.Todo
import slick.driver.MySQLDriver.api._

class TodosTable(tag: Tag) extends Table[Todo](tag, "todos"){
  def uuid = column[String]("uuid", O.PrimaryKey)
  def title = column[String]("title")
  def description = column[String]("description")
  def done = column[Boolean]("done")
  def * = (uuid.?, title, description, done) <> ((Todo.apply _).tupled, Todo.unapply)
}

