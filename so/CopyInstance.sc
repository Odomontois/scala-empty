

trait CopyMaker[T <: CopyMaker[T]] {
  def makeCopy: T
}

case class B(name: String) extends CopyMaker[B]

case class C(name: String) extends CopyMaker[C]
