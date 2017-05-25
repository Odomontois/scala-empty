import com.softwaremill.macwire._

case class User(email: String)

trait UserRepository {
  def findByEmail(email: String): User
}

class AccountService(val userRepo: UserRepository)

class UserRepositoryImpl extends UserRepository{
  def findByEmail(email: String): User = new User(email)
}

class AccountServiceSpec {
  val userRepo = new UserRepositoryImpl()
  val accountSvc = wire[AccountService] //<--not manually injecting repo in service constructor
}