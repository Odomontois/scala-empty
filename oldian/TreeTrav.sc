import scalaz.ListT._
import scalaz.State._
import scalaz._
import scalaz.std.list._
import scalaz.syntax.monad._
import scalaz.syntax.std.option._
import scalaz.syntax.traverse._

import scala.Integral.Implicits._
import scala.Ordering.Implicits._

import spire._
import spire.syntax.literals.si._
import Function.uncurried
val LT = implicitly[MonadTrans[ListT]]
type Store[I] = (I, Boolean)
type SearchState[I, X] = State[Store[I], X]
type Iter[I, X] = ListT[SearchState[I, ?], X]
def liftIter[I, X](y: SearchState[I, X]): Iter[I, X] = LT.liftM[SearchState[I, ?], X](y)
case class IterDef[I, X](gen: (X, I) ⇒ (List[X], I),
                         pred: X ⇒ Boolean)
def searchIter[I, X](idef: IterDef[I, X])(x: X): Iter[I, X] = for {
  (having, found) ← liftIter(get[(I, Boolean)]) if !found
  (next, inew) = idef.gen(x, having)
  _ ← liftIter(modify[(I, Boolean)] { case (i, f) ⇒ (inew, false) })
  u ← ListT.fromList[SearchState[I, ?], X](next.point[SearchState[I, ?]])
} yield u

implicit def implying[I] = listTMonadPlus[SearchState[I, ?]]
def buildSearch[I, X](start: X, idef: IterDef[I, X]) =
  Cofree.unfoldC[Iter[I, ?], X](start)(searchIter(idef))

def executeSearch[I, X](struct: Cofree[Iter[I, ?], X], pred: X ⇒ Boolean) =
  struct.scanr[SearchState[I, Option[X]]] {
    case (head, _) if pred(head) ⇒ modify[Store[I]] { case (s, _) ⇒ (s, true) } map (_ ⇒ head.some)
    case (_, tail) ⇒ for {
      seq ← tail.run
      lst ← seq.map(_.head).sequence[SearchState[I, ?], Option[X]]
    } yield lst.find(_.isDefined).flatten
  }
def search[I, X](start: X, initial: I, idef: IterDef[I, X]) =
  executeSearch(buildSearch[I, X](start, idef), idef.pred).head.run((initial, false))

def backSearch[N: Integral](start: N, target: N): ((Set[N], Boolean), Option[(N, List[N])]) = {
  val N = implicitly[Numeric[N]]
  val two = N.fromInt(2)
  def gen(x: N, way: List[N])(seen: Set[N]) = {
    val next =
        (x % two == N.zero, x / two) ::
      (x > N.one, x - N.one) ::
        (x > two, x - two) ::
        Nil collect { case (true, y) if !seen(y) ⇒ y }
    (next.map(y ⇒ (y, y :: way)), seen ++ next)
  }
  search[Set[N], (N, List[N])](
    (start, List(start)), Set.empty,
    IterDef(uncurried((gen _).tupled), _._1 == target))
}


backSearch(123, 4) match {case ((set, _), elem) ⇒ (set.size, elem.map(_._2).getOrElse(Nil))}

