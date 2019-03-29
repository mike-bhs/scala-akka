package dao

import models.Todo

import scala.concurrent.Future
import slick.driver.MySQLDriver.api._

object TodosDao extends BaseDao {
  def findAll: Future[Seq[Todo]] = todosTable.result
}