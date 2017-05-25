import cats._
import cats.data.OptionT
import cats.instances.future._
import cats.syntax.transLift._
import cats.syntax.applicative._
import cats.syntax.monadFilter._
import cats.instances.option._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


type FOpt[x] = OptionT[Future, x]
case class User(id: Int)
def getUser(id: Int): Future[Option[User]] = id.pure[Option].filter(_ > 0).map(User).pure[Future]
def getUser1(id: Int): FOpt[User] = id.pure[FOpt].filter(_ > 0).map(User)
def getUser2(id: Int): FOpt[User] = OptionT(getUser(id))
def foo(user: User) = user.toString

def userString(id: Int) = for {
  user ← getUser2(id)
} yield foo(user)

Await.result(userString(1).value, 1 second)
Await.result(userString(-1).value, 1 second)