import spire.algebra.{Field, AdditiveAbGroup}
import shapeless._
import shapeless.Nat._

implicit case class Element[M, D](value: Int)

trait FiniteField[M, D] extends Field[Element[M, D]]


implicit def degeree1PrimeFld[M <: Nat](implicit M: Witness.Aux[M]) =
  new FiniteField[M, _1] {
    val m = toInt(M.value)

    def quot(a: Element[M, Nat._1], b: Element[M, Nat._1]): Element[M, Nat._1] = ???

    def gcd(a: Element[M, Nat._1], b: Element[M, Nat._1]): Element[M, Nat._1] = ???

    def mod(a: Element[M, Nat._1], b: Element[M, Nat._1]): Element[M, Nat._1] = ???

    def plus(x: Element[M, Nat._1], y: Element[M, Nat._1]): Element[M, Nat._1] = (x.value + y.value) % m

    def div(x: Element[M, Nat._1], y: Element[M, Nat._1]): Element[M, Nat._1] = ???

    def one: Element[M, Nat._1] = 1

    def times(x: Element[M, Nat._1], y: Element[M, Nat._1]): Element[M, Nat._1] = (x.value * y.value) % m

    def negate(x: Element[M, Nat._1]): Element[M, Nat._1] = (- x.value) % m

    def zero: Element[M, Nat._1] = 0
  }


