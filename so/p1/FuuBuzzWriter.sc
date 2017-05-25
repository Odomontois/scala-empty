/** http://stackoverflow.com/questions/35601077/scala-transform-a-collection-yielding-0-many-elements-on-each-iteration
  */
for {
  n <- 0 to 15
  d <- Seq(3, 5) if n % d == 0
} yield Seq(s"$n/$d")

import scalaz.Writer
import scalaz.syntax.writer._
import scalaz.syntax.monad._
import scalaz.std.vector._
import scalaz.syntax.traverse._

type Messages[T] = Writer[Vector[String], T]

def yieldW(a: String): Messages[Unit] = Vector(a).tell

Vector.range(0, 15).traverse { n =>
  yieldW(s"$n / 3").whenM(n % 3 == 0) >>
    yieldW(s"$n / 5").whenM(n % 5 == 0)
}.run._1