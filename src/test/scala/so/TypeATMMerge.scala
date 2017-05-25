package so


/**
  * User: Oleg
  * Date: 26-Feb-16
  * Time: 15:46
  */
object TypeATMMerge extends App{
  trait L
  trait R
  trait Base {
    type T
    def get: T
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
  trait Third[B <: Base] extends Base{

  }
  val concrete = new (Left ~+~ Right) {
    def get: T = new L with R {}
  }

  val x1: L with R = concrete.get
  val x2: R = concrete.asInstanceOf[Left ~+~ Right].get
  val x3: L = concrete.asInstanceOf[Right ~+~ Left].get
}
