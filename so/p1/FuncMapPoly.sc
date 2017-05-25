import scala.reflect.runtime.universe._
def foo(string: String): Seq[String] = {
  Seq(string)
}

type A = Fun

def bar(string: String): Seq[Int] = {
  Seq(1)
}

class PolyMap[K, V[+ _]] {
  var backing = Map[(K, TypeTag[_]), V[Any]]()

  def apply[T: TypeTag](key: K) =
    backing.get(key, typeTag[T]).asInstanceOf[Option[V[T]]]

  def update[T: TypeTag](key: K, f: V[T]): this.type = {
    backing += (key, typeTag[T]) → f
    this
  }
}
type String2Seq[+X] = String ⇒ Seq[X]
val polyMap = new PolyMap[String, String2Seq]

polyMap("foo") = foo
polyMap("bar") = bar


polyMap[String]("foo").map(_("x")) == Some(foo("x"))
polyMap[Int]("foo").map(_("x")) == None
polyMap[Int]("bar").map(_("x")) == Some(bar("x"))