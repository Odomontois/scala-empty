sealed abstract class Tree[+T]

sealed abstract class Node[+T] extends Tree[T] {
  def value: T
  def left: Tree[T]
  def right: Tree[T]
}

case class SimpleNode[+T](value: T, left: Tree[T], right: Tree[T])
  extends Node[T] {
  override def toString = "T(" + value.toString + " " + left.toString + " " + right.toString + ")"
}

case object End extends Tree[Nothing] {
  override def toString = "."
}

case class PositionedNode[+T](val value: T, val left: Tree[T], val right: Tree[T], x: Int, y: Int)
  extends Tree[T] {
  override def toString = s"T[$x, $y]($value $left $right)"
}

object Node {
  def apply[T](value: T, left: Tree[T], right: Tree[T]) = SimpleNode(value, left, right)
  def unapply[T](node: Tree[T]): Option[(T, Tree[T], Tree[T])] = node match {
    case SimpleNode(v, l, r) ⇒ Some((v, l, r))
    case PositionedNode(v, l, r, _, _) ⇒ Some((v, l, r))
    case _ ⇒ None
  }
}



