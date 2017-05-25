import scala.collection.mutable.ArrayBuffer
import scala.runtime.AbstractFunction0
val f: Int => String = x => "a"
val g: (=> Int) => String = x => "b"

case class ParamConvertible[X, Y, T](apply: ((Y => T) => X => T))

implicit def idParamConvertible[X, T] = ParamConvertible((f: X => T) => (x: X) => f(x))

implicit def byNameParamConvertible[X, T] = ParamConvertible((f: (=> X) => T) => (x: X) => f(x))

def t[T](h: T => String)
        (implicit conv: ParamConvertible[Int, T, String]): String =
  conv.apply(h)(42)

t(f)

t(g)

def a(b: ArrayBuffer[Int]): Unit ={
  b.companion
}