/**
  * Author: Oleg Nizhnik
  * Date  : 09.12.2015
  * Time  : 9:31
  */
package so
import scalaz._

trait CurrencyUnit {
  def show(amounts: String) = s"$amounts $this"
}

object CurrenciesMonocle {
  import Isomorphism._
  import scalaz.syntax.show._

  final case class Currency[A, U <: CurrencyUnit](amount: A) extends AnyVal

  implicit case object GBP extends CurrencyUnit
  implicit case object USD extends CurrencyUnit {
    override def show(amounts: String) = s"$$$amounts "
  }
  implicit case object EUR extends CurrencyUnit

  implicit class CurrencyOps[A](a: A) {
    def GBP = Currency[A, CurrenciesMonocle.GBP.type](a)
    def EUR = Currency[A, CurrenciesMonocle.EUR.type](a)
    def USD = Currency[A, CurrenciesMonocle.USD.type](a)
  }

  implicit def currencyShow[A: Show, U <: CurrencyUnit](implicit unit: U) = new Show[Currency[A, U]] {
    override def shows(f: Currency[A, U]) = unit.show(f.amount.shows)
  }

implicit def currencyIso[A, U <: CurrencyUnit] = new (Currency[A, U] <=> A) {
  def to: (Currency[A, U]) => A = _.amount
  def from: (A) => Currency[A, U] = Currency[A, U]
}

implicit def currencyMonoid[A: Monoid, U <: CurrencyUnit] =
  new IsomorphismMonoid[Currency[A, U], A] {
    def G: Monoid[A] = implicitly
    def iso: Currency[A, U] <=> A = implicitly
  }

  def main(args: Array[String]) {

    import scalaz.syntax.monoid._
    import scalaz.std.anyVal._

    println((1.USD |+| 3.USD).shows)
  }
}



