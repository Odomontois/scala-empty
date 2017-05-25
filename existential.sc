trait A {
  type T
  def test(t: T): Unit
}

trait A2 extends A{
  type T <: AnyRef
}

class A3 extends A2{
  override type T = Integer

  def test(t: Integer): Unit = println(t * 2)
}

case class B[S <: A, ST](a: S {type T = ST}, t: ST) {
  def test() = a.test(t) // Error: type mismatch;
  // found   : B.this.t.type (with underlying type S#T)
  // required: B.this.a.T
}
