package utils

trait DatabaseConfig {
  val driver = slick.driver.MySQLDriver

  import driver.api._

  val url = "jdbc:mysql://localhost:3306/scala_tryout?useUnicode=true&characterEncoding=UTF-8"
  val user = "root"
  val password = ""
  val driverName = "com.mysql.cj.jdbc.Driver"

  def db = Database.forURL(url=url, user=user, password=password, driver=driverName)

  implicit val session: Session = db.createSession()
}