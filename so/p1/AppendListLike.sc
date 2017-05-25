trait ListLike[+E, T[+X] <: ListLike[X, T]] {
  def +:[E1 >: E](x: E1): T[E1]

  def foldLeft[X](z: X)(f: (X, E) => X): X

  def empty: T[E]

  def reverse: T[E] = foldLeft(empty)((list, x) => x +: list)
}

implicit class ListLikeOps[E, T[+X] <: ListLike[X, T] ](val lst: ListLike[E, T]) extends AnyVal{
  def ++[E1 >: E](that: T[E1]): T[E1] = lst.reverse.foldLeft(that)((list, x) => x +: list)
}

sealed trait MyList[+E] extends ListLike[E, MyList] {
  def +:[E1 >: E](x: E1): MyList[E1] = MyCons(x, this)

  def empty = MyNil

  def foldLeft[X](z: X)(f: (X, E) => X): X = {
    def go(z: X, lst: MyList[E]): X = lst match {
      case MyNil => z
      case MyCons(x, next) => go(f(z, x), next)
    }
    go(z, this)
  }

  override def toString = foldLeft("MyList(")( _ + _ + ",") + ")"
}

case object MyNil extends MyList[Nothing]

case class MyCons[+E](elem: E, next: MyList[E]) extends MyList[E]

(1 +: 2 +: 3 +: MyNil) ++ (4 +: 5 +: MyNil)
