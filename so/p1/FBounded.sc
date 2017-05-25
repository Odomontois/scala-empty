import scala.language.existentials

trait FBounded[IMPL <: FBounded[IMPL]] { self: IMPL =>
  def foo: IMPL
}

trait FBoundedUser[F <: FBounded[F]] {
  def bar(value: F): F = value.foo
}

trait SimpleImpl extends FBounded[SimpleImpl] {
  override def foo: SimpleImpl = this
}

object SimpleUser extends FBoundedUser[SimpleImpl]

// A-OK so far...

trait ComplexImpl[IMPL <: ComplexImpl[IMPL]] extends FBounded[IMPL] { self: IMPL =>
  def baz: IMPL
}

object ComplexImpl {
  type AnyComplexImpl = ComplexImpl[T] forSome { type T <: ComplexImpl[T] }
}


import ComplexImpl.AnyComplexImpl
object ComplexUser1 extends FBoundedUser[ComplexImpl[_]]
object ComplexUser2 extends FBoundedUser[AnyComplexImpl]