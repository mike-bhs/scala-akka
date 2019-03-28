import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class TodoRouterCreateSpec extends WordSpec with Matchers with ScalatestRouteTest with TodoMocks {
  // Required to make akka be able to parse todos to json
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  val testCreateTodo = CreateTodo("test title", "test description")

  "A TodoRouter" should {
    "create a todo with valid data" in {
      val repository = new InMemoryTodoRepository()
      val router = new TodoRouter(repository)


      Post("/todos", testCreateTodo) ~> router.route ~> check {
        status shouldBe StatusCodes.OK

        val resp = responseAs[Todo]

        resp.title shouldBe testCreateTodo.title
        resp.description shouldBe testCreateTodo.description
      }
    }

    "not create a todo with invalid data" in {
      val repository = new FailingRepository()
      val router = new TodoRouter(repository)
      val invalidTodo = CreateTodo("", "")


      Post("/todos", invalidTodo) ~> router.route ~> check {
        status shouldBe StatusCodes.UnprocessableEntity

        val resp = responseAs[String]

        resp shouldBe "Invalid Todo"
      }
    }
  }
}
