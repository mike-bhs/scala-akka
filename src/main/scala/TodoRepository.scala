import java.util.UUID

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

trait TodoRepository {
  def all(): Future[Seq[Todo]]
  def done(): Future[Seq[Todo]]
  def pending(): Future[Seq[Todo]]
  def save(createTodo: CreateTodo): Future[Todo]
}

case class InMemoryTodoRepository(initialTodos: Seq[Todo] = Seq.empty)(implicit ec: ExecutionContext) extends TodoRepository {
  private var todos: Vector[Todo] = initialTodos.toVector

  override def all(): Future[Seq[Todo]] = successful(todos)
  override def done(): Future[Seq[Todo]] = successful(todos.filter(_.done))
  override def pending(): Future[Seq[Todo]] = successful(todos.filterNot(_.done))

  override def save(createTodo: CreateTodo): Future[Todo] = successful {
    val todo = Todo(
      UUID.randomUUID().toString(),
      createTodo.title,
      createTodo.description,
      false
    )

    todos = todos :+ todo
    todo
  }
}
