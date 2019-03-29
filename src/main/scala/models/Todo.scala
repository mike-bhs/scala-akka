package models

case class Todo(uuid: Option[String], title: String, description: String, done: Boolean)
