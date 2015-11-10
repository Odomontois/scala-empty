import shapeless._
import ops._
import poly._

sealed trait ElementOf[L <: HList, X] {
  def apply(l: L): X
}

class HeadElement[L <: HList, X] extends ElementOf[X :: L, X] {
  def apply(l: X :: L) = l.head
}

class RecurseElement[L <: HList, Y, X](implicit e: ElementOf[L, X]) extends ElementOf[Y :: L, X] {
  def apply(l: Y :: L) = e(l.tail)
}

trait EvidenceMap[H <: HList, T[_]] {
  type Out <: HList
  def out: Out

  def mapTC[X](elem: ElementOf[H, X]): ElementOf[Out, T[X]]
}

object EvidenceMap {

  implicit def headElement[L <: HList, X] = new HeadElement[L, X]
  implicit def recurseElement[L <: HList, Y, X](implicit e: ElementOf[L, X]) =
    new RecurseElement[L, Y, X]

  type Aux[H <: HList, T[_], O] = EvidenceMap[H, T] {type Out = O}

  implicit def HNilEvidence[T[_]] = new EvidenceMap[HNil, T] {
    type Out = HNil
    val out = HNil
    def mapTC[X](elem: ElementOf[HNil, X]): ElementOf[HNil, T[X]] = ???
  }
  implicit def HListEvidence[Head, Remaining <: HList, T[_]]
  (implicit headEv: T[Head],
   remainingEv: EvidenceMap[Remaining, T]) =
    new EvidenceMap[Head :: Remaining, T] {
      type Out = T[Head] :: remainingEv.Out
      val out = headEv :: remainingEv.out
      def mapTC[X](elem: ElementOf[Out, X]): ElementOf[T[Head] :: remainingEv.Out, T[X]] =
        elem match {
          case head: HeadElement[Head, ]
        }
    }
}

//def transform[A](a: A)(implicit ev: EvidenceMap.Aux[A :: HNil, Ordering, Ordering :: HNil]) = { List(a, a).sorted }

val a = 1 :: 2 :: HNil
a.select

class XX[T[_]] {

}