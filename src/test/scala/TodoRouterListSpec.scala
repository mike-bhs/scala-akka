import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class TodoRouterListSpec extends WordSpec with Matchers with ScalatestRouteTest with TodoMocks {
  // Required to make akka be able to parse todos to json
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  private val doneTodo = Todo("1", "title1", "description1", done = true)
  private val pendingTodo = Todo("2", "title1", "description1", done = false)
  private val todos = Seq(doneTodo, pendingTodo)

  "TodoRouter" should {

    "return all the todos" in {
      val repository = InMemoryTodoRepository(todos)
      val router = TodoRouter(repository)

      Get("/todos") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[Todo]]
        response shouldBe todos
      }
    }

    "return all the done todos" in {
      val repository = InMemoryTodoRepository(todos)
      val router = TodoRouter(repository)

      Get("/todos/done") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[Todo]]
        response shouldBe Seq(doneTodo)
      }
    }

    "return all the pending todos" in {
      val repository = InMemoryTodoRepository(todos)
      val router = TodoRouter(repository)

      Get("/todos/pending") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[Todo]]
        response shouldBe Seq(pendingTodo)
      }
    }

    "handle repository failure in the todos route" in {
      val repository = new FailingRepository
      val router = TodoRouter(repository)

      Get("/todos") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
        val resp = responseAs[String]
        resp shouldBe "Unknown error"
      }
    }

    "handle repository failure in the done todos route" in {
      val repository = new FailingRepository
      val router = TodoRouter(repository)

      Get("/todos/done") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }

    "handle repository failure in the done pending route" in {
      val repository = new FailingRepository
      val router = TodoRouter(repository)

      Get("/todos/pending") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }
  }
}
