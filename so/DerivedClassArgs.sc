class A(arg: String)

abstract class B(arg: String) extends A(arg) {
  def argList: IndexedSeq[String]
}

object B {
  case object Empty extends B("") {
    def argList = IndexedSeq.empty
  }
  case class NonEmpty private[B](argList: Vector[String]) extends B(argList.head)

  def apply(argList: Vector[String]) =
    if (argList.isEmpty) Empty else NonEmpty(argList)

  def unapplySeq(b:B): Option[IndexedSeq[String]] = b match {
    case Empty ⇒ Some(IndexedSeq.empty)
    case NonEmpty(args) ⇒ Some(args)
  }
}

B(Vector("1","2","3")) match {
  case B(a,b,_*) ⇒ (a,b)
}

B(Vector("x", "y")).isInstanceOf[B.NonEmpty]