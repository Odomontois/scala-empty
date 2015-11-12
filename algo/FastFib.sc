import Numeric.Implicits._
import Integral.Implicits._
def fastFib[N: Numeric, I: Integral](k: I, start: Option[(N, N)] = None): N = {
  val N = implicitly[Numeric[N]]
  val I = implicitly[Numeric[I]]
  val twoI = I.fromInt(2)
  val twoN = N.fromInt(2)

  def getBits(k: I, acc: List[I] = Nil): List[I] =
    if (k == I.zero) acc
    else {
      val (m, d) = k /% twoI
      getBits(m, d :: acc)
    }

  def step(f: (N, N), b: I): (N, N) = {
    val (fk, fk1) = f
    val f2k = fk * (fk1 * twoN - fk)
    val f2k1 = fk1 * fk1 + fk * fk
    def f2k2 = f2k + f2k1
    if (b == I.zero) (f2k, f2k1) else (f2k1, f2k2)
  }

  val begin = start.getOrElse(N.zero, N.one)

  getBits(k).foldLeft(begin)(step)._1
}

fastFib[BigInt, Int](1234)