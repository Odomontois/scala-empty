import scalaz._
import scalaz.syntax.foldable._
import scalaz.std.tuple._
import scalaz.std.vector._
import scalaz.std.list._
import scalaz.syntax.std.tuple._
import scalaz.syntax.monoid._


class A
case class B() extends A
case class C() extends A
case class D() extends A
val lst = List(B(), C(), B(), D(), B()).multiply(3)
def f2(list: List[A]) = list foldMap {
  case b: B => (List(b), Nil, Nil)
  case c: C => (Nil, List(c), Nil)
  case d: D => (Nil, Nil, List(d))
}

def f(list: List[A]) = list.foldMap {
  case b: B => (Vector(b), Vector( ), Vector( ))
  case c: C => (Vector( ), Vector(c), Vector( ))
  case d: D => (Vector( ), Vector( ), Vector(d))
} mapElements(_.toList, _.toList, _.toList)
f(lst)

lst.foldLeft((List.empty[B], List.empty[C],List.empty[D])) { case ((bs, cs, ds), a) =>
  a match {
    case x: B => (x :: bs, cs, ds)
    case x: C => (bs, x :: cs, ds)
    case x: D => (bs, cs, x :: ds)
  }
}

(1 to 5).foldLeft(0) { case (x, y) => x + y}

