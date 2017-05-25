import scalaz._
import shapeless._
import scalaz.syntax.monoid._
import scalaz.std.anyVal._

sealed trait Currency
final case class GBP[A](amount: A) extends Currency
final case class USD[A](amount: A) extends Currency
final case class EUR[A](amount: A) extends Currency



}

2 |+| 3
