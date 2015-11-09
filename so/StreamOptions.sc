import scalaz._
import syntax.monad._
import std.option._
import syntax.std.option._

type StreamO[X] = StreamT[Option, X]


type A = Int
type B = String
def makeBs(a: A) = for (x <- a.point[StreamO] if x % 2 == 1) yield x.toString * x

StreamT.Yield

val x = Stream(3)

StreamT.fromStream(x.some)
def handleNestedStream(as: Stream[A]): StreamO[(A, B)] = for {
  a <- StreamT fromStream as.some
  b <- makeBs(a)
} yield (a, b)

handleNestedStream(1 to 5 toStream).toStream