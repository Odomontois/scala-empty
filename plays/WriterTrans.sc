import cats.std.stream._
import cats.syntax.flatMap._
import cats.syntax.traverse._
import cats.data._
import cats._
import Fractional.Implicits._
import Numeric.Implicits._

case class Average[N](count: N, sum: N) {
  def average(implicit N: Fractional[N]) = sum / count
}
object Average {
  def apply[N](value: N)(implicit N: Numeric[N]): Average[N] = Average(N.one, value)
}

implicit def averageMonoid[N](implicit N: Numeric[N]) = new Monoid[Average[N]] {
  def empty: Average[N] = Average(N.zero, N.zero)

  def combine(x: Average[N], y: Average[N]): Average[N] = Average(x.count + y.count, x.sum + y.sum)
}

type Writee = Average[Double]
type FS[A] = WriterT[Eval, Writee, A]
val monoid = Monoid[Writee]
implicit val FS = WriterT.writerTMonadWriter[Eval, Writee]


val stream: Stream[FS[Int]] = Stream.range(1, 100000).map { x =>
  val rest = x % 3
  val res = FS.pure(rest)
  if (rest != 0) FS.tell(Average(rest.toDouble)) followedBy res else res
}

stream.sequenceU.written.value
