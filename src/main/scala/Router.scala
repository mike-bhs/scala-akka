import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}

import scala.util.{Failure, Success}

trait Router {
  def route: Route
}

case class TodoRouter(todoRepository: TodoRepository) extends Router with Directives {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route: Route = pathPrefix("todos") {
    pathEndOrSingleSlash {
      get {
        onComplete(todoRepository.all()) {
          case Success(todos) =>
            complete(todos)
          case Failure(err) =>
            println(err.getMessage)
            complete(StatusCodes.InternalServerError, "Unknown error")
        }
      }
    } ~ path("done") {
      get { complete(todoRepository.done()) }
    } ~ path("pending") {
      get { complete(todoRepository.pending()) }
    }
  }
}