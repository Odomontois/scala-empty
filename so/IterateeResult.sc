import play.api.libs.iteratee.{Enumeratee, Enumerator, Iteratee}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scalaz.{Traverse, Foldable, Monad}
import scalaz.syntax.foldable1._
import scalaz.std.list._

sealed trait Result[+T]
case object Empty extends Result[Nothing]
case class Error(cause: Throwable) extends Result[Nothing]
case class Success[T](value: T) extends Result[T]

val good = Enumerator.enumerate[Result[Int]](
  Seq(Success(1), Empty, Success(2), Success(3)))

val bad = Enumerator.enumerate[Result[Int]](
  Seq(Success(1), Success(2), Error(new Exception("uh oh")), Success(3)))

val toList = Enumeratee.map[Result[Int]] {
  case Success(x) => List(x)
  case _ => Nil
} transform Iteratee.consume[List[Int]]()

implicit object resultInstance extends Monad[Result] {
  def point[A](a: => A): Result[A] = Success(a)
  def bind[A, B](fa: Result[A])(f: (A) => Result[B]): Result[B] = fa match {
    case Success(x) => f(x)
    case err: Error => err
    case Empty => Empty
  }
}

def isError[X]: Result[X] => Boolean = {
  case Error(_) => true
  case _ => false
}


def listResults[X] =
  Enumeratee.scanLeft[Result[X]]((true, Empty: Result[X])) { (prev, that) =>
      (!isError(prev._2), that)
    } ><>
    Enumeratee.takeWhile(_._1) ><>
    Enumeratee.map(_._2) ><>
    Enumeratee.filter {
      case Empty => false
      case _ => true
    } ><>
    Enumeratee.map(List(_)) &>>
    Iteratee.consume() map {Traverse[List].sequence(_)}

def runRes[X](e: Enumerator[Result[X]]) = Await.result(e.run(listResults), 3 seconds)

runRes(good)
runRes(bad)

System.out


