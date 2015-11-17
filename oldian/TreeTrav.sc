import scalaz._
import State._
import syntax.monad._
import ListT._
import std.list._
import syntax.std.option._
import std.option._
import syntax.traverse._
import syntax.monoid._
import std.set.setMonoid
import std.anyVal._
val LT = implicitly[MonadTrans[ListT]]
type Store[I] = (I, Boolean)
type SearchState[I, X] = State[Store[I], X]
type Iter[I, X] = ListT[SearchState[I, ?], X]
def liftIter[I, X](y: SearchState[I, X]): Iter[I, X] = LT.liftM[SearchState[I, ?], X](y)
case class IterDef[I, X](gen: X ⇒ List[X],
                         check: I ⇒ X ⇒ Boolean,
                         trans: List[X] ⇒ I,
                         pred: X ⇒ Boolean)
def searchIter[I: Monoid, X](idef: IterDef[I, X])(x: X): Iter[I, X] = for {
  (having, found) ← liftIter(get[(I, Boolean)]) if !found
  next = idef.gen(x).filter(idef.check(having))
  _ ← liftIter(modify[(I, Boolean)] { case (i, f) ⇒ (i |+| idef.trans(next), false) })
  u ← ListT.fromList[SearchState[I, ?], X](next.point[SearchState[I, ?]])
} yield u

implicit def implying[I] = listTMonadPlus[SearchState[I, ?]]
def buildSearch[I: Monoid, X](start: X, idef: IterDef[I, X]) =
  Cofree.unfoldC[Iter[I, ?], X](start)(searchIter(idef))

def executeSearch[I, X](struct: Cofree[Iter[I, ?], X], pred: X ⇒ Boolean) =
  struct.scanr[SearchState[I, Option[X]]] {
    case (head, _) if pred(head) ⇒ modify[Store[I]] { case (s, _) ⇒ (s, true) } map (_ ⇒ head.some)
    case (_, tail) ⇒ for {
      seq ← tail.run
      lst ← seq.map(_.head).sequence[SearchState[I, ?], Option[X]]
    } yield lst.find(_.isDefined).flatten
  }
def search[I: Monoid, X](start: X, idef: IterDef[I, X]) =
  executeSearch(buildSearch[I, X](start, idef), idef.pred).head.run((mzero[I], false))
def boundSearch(start: Long, target: Long) =
  search[Set[Long], Long](start, IterDef(x ⇒ List(x * 2, x * 3), s => !s(_), _.toSet, _ > target))

boundSearch(1, 7)

import Integral.Implicits._
import scala.Ordering.Implicits._

def backSearch[N: Integral](start: N, target: N): ((Set[N], Boolean), Option[(N, Int)]) = {
  val N = implicitly[Numeric[N]]
  val two = N.fromInt(2)
  def gen(x: N, depth: Int) = List(
    (x % two == N.zero, x / two),
    (x > N.one, x - N.one),
    (x > two, x - two)
  ).collect { case (true, y) ⇒ (y, depth + 1) }
  search[Set[N], (N, Int)](
    (start, 0),
    IterDef(
      (gen _).tupled,
      has ⇒ {case (x, iters) ⇒ !has(x)},
      _.map(_._1).toSet,
      _._1 == target))
}

def forwardSearch[N: Integral](start: N, target: N): ((Set[N], Boolean), Option[(N, List[N])]) = {
  val N = implicitly[Numeric[N]]
  val two = N.fromInt(2)
  def gen(x: N, way: List[N]) = List(
    (x % two == N.zero, x / two),
    (x > N.one, x - N.one),
    (x > two, x - two)
  ).collect { case (true, y) ⇒ (y, x :: way) }
  search[Set[N], (N, List[N])](
    (start, Nil),
    IterDef(
      (gen _).tupled,
      has ⇒ {case (x, iters) ⇒ !has(x)},
      _.map(_._1).toSet,
      _._1 == target))
}


forwardSearch(3000000, 1) match {case ((set, found), elem) ⇒ (set.size, found, elem)}