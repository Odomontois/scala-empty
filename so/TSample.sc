case class A[T](a:T, b:Array[Int])
object A {
  def apply[T](aa:T):A[T] = A(aa, Array(1, 2, 3))
}

trait TLike[T]
case class TSample1[T](str:String) extends TLike[T]

A(TSample1("dumb"))