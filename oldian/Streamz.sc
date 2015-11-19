import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scalaz._
import scalaz.Id.{Id, Identity}
import scala.concurrent.ExecutionContext.Implicits._
import StreamT._
import scalaz.syntax.monad._
import scalaz.std.scalaFuture._

def iterate[M[_] : Monad](fx: M[Int], lim: Int): M[Stream[Int]] = {
  def cons(x: Int, xs: Stream[Int]) = x #:: xs

  fx flatMap { x =>
    if (x > lim) Stream[Int]().point[M]
    else
      fx.flatMap(x => Applicative[M].apply2(fx, iterate(fx.map(_ + 1), lim))(cons _))
  }
}
Await.result(iterate(Future(1), 20000), Duration.Inf).drop(10000).head
//iterate[Id](1, 20000).drop(10000).head
val u = StreamT.unfoldM[Identity, Int, Int](1)( x => Need(if (x < 20000) Some(x+1,x+1) else None ))
u.drop(10000).head.value
