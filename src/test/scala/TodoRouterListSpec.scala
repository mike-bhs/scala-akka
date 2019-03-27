import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class TodoRouterListSpec extends WordSpec with Matchers with ScalatestRouteTest {
  // Required to make akka be able to parse todos to json
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  private val doneTodo = Todo("1", "title1", "description1", done = true)
  private val pendingTodo = Todo("2", "title1", "description1", done = false)
  private val todos = Seq(doneTodo, pendingTodo)

  "TodoRouter" should {
    "return all todos" in {
      val repository = InMemoryTodoRepository(todos)
      val router = TodoRouter(repository)

      Get("/todos") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[Todo]]
        response shouldBe todos
      }
    }
  }
}
