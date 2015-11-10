class X(val x: BigInt) {
  def \+/+\(that: X) = new X(this.x + that.x - (this.x gcd that.x))

  override def toString = "X"+x
}

object X {
  def apply[T : ({type λ[X] = X ⇒ BigInt})#λ](x: T) = new X(x)
}

var a = X(3)
a \+/+\= X(4)
a

