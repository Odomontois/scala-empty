import scala.util.Try
import scalaz.syntax.{BindOps, MonadOps}

trait L
trait R
trait Base {
  type T
}
trait Nil extends Base{
  type T = Any
}
trait ~+~[X[_ <: Base] <: Base, Y[_ <: Base] <: Base] extends Base {
  type T = Y[X[Nil]]#T
}
trait Left[B <: Base] extends Base {
  type T = B#T with L
  def get: T
}
trait Right[B <: Base] extends Base {
  type T = B#T with R
}
val concrete = new (Left ~+~ Right) {
  def get: T = new L with R {}
}
val x1: L with R = concrete.get
//val x2: L with R = concrete.asInstanceOf[Left with Right].get
//val x3: L = concrete.asInstanceOf[Right with Left].get
type E = BindOps
