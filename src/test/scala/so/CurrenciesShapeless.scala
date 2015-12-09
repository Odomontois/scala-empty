/**
  * Author: Oleg Nizhnik
  * Date  : 09.12.2015
  * Time  : 10:18
  */
package so

import scalaz._
import shapeless._
import scalaz.syntax.monoid._
import scalaz.std.anyVal._

object currency {
  sealed trait Currency extends Any
  final case class GBP[A](amount: A) extends AnyVal with Currency
  final case class USD[A](amount: A) extends AnyVal with Currency
  final case class EUR[A](amount: A) extends AnyVal with Currency

  implicit class CurrencyOps[A](a: A) {
    def GBP = currency.GBP(a)
    def EUR = currency.EUR(a)
    def USD = currency.USD(a)
  }

implicit def monoidCurrency[A, C[_] <: Currency]
(implicit monoid: Monoid[A], gen: Generic.Aux[C[A], A :: HNil]) =
  new Monoid[C[A]] {
    def zero: C[A] = gen.from(monoid.zero :: HNil)
    def append(f1: C[A], f2: => C[A]): C[A] = {
      val x = gen.to(f1).head
      val y = gen.to(f2).head
      gen.from(monoid.append(x, y) :: HNil)
    }
  }
}





object CurrenciesShapeless {
  import currency._
  def main(args: Array[String]) {
    println(2.USD |+| 3.USD)
  }
}
