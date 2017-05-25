import scalaz._
import scalaz.:+:.{inL, inR}
import scalaz.std.anyVal._
import scalaz.std.list._
val u = :+:.empty[Int, Int] |+|
  inL(1) |+| inL(2) |+| inR(4) |+| inL(2)
u.fold(x ⇒ List("left" → x), y ⇒ List("right" → y))

