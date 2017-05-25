trait Bar[I] {def doSomething(x: I): String}
object Doer {
  def apply[I <: Foo](el: I)(implicit ev: Bar[I]) = ev.doSomething(el)
}

trait Foo

case class A() extends Foo
object A {
  implicit object Something extends Bar[A] {
    def doSomething(el: A) = "do A"
  }
}

case class B() extends Foo
object B {
  implicit object SomethingElse extends Bar[B] {
    def doSomething(el: B) = "do B"
  }
}

abstract class MemoTC[Abstract, TC[_]] {
  type Concrete <: Abstract
  val value: Concrete
  val inst: TC[Concrete]

  def withTC[Result](action: (Concrete) ⇒ TC[Concrete] ⇒ Result): Result =
    action(value)(inst)
}

object MemoTC {
  def apply[A, C <: A, TC[_]](v: C)(implicit tc: TC[C]) = new MemoTC[A, TC] {
    type Concrete = C
    val value = v
    val inst = tc
  }
}


val xs: List[MemoTC[Foo, Bar]] = List(MemoTC(A()), MemoTC(B()))
xs.map(x => x.withTC(x ⇒ implicit bar ⇒ Doer(x)))

