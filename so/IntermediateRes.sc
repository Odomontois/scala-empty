import scala.util.Try
val g1: Int => Option[Int] = x => if (x % 2 == 1) None else Some(x / 2)
val g2: Int => Option[Int] = x => Some(x * 3 + 1)
val g3: Int => Option[Int] = x => if (x >= 4) Some(x - 4) else None
val f1: Int => Option[String] = x => Some(x.toString)
val f2: String => Option[Int] = x => Try(x.toInt).toOption
val f3: Int => Option[Int] = x => if (x % 2 == 1) None else Some(x / 2)
def bind[T]: (Option[T], T => Option[T]) => Option[T] = _ flatMap _
def chain[T](x: T, fs: List[T => Option[T]]) = fs.scanLeft(Some(x): Option[T])(bind)
chain(4, g1 :: g2 :: g3 :: Nil)
import shapeless._
import ops.hlist.LeftScanner._
import shapeless.ops.hlist._
object hBind extends Poly2 {
  implicit def bind[T, G] = at[T => Option[G], Option[T]]((f, o) => o flatMap f)
}
def hChain[Z, L <: HList](z: Z, fs: L)
                         (implicit lScan: LeftScanner[L, Option[Z], hBind.type]) =
  lScan(fs, Some(z))
//hIntermed(4 : Int, f1 :: f2 :: f3 :: HNil)
hChain(4, f1 :: f2 :: f3 :: HNil)

case class Result(init: Option[Int],
                  x1: Option[String],
                  x2: Option[Int],
                  x3: Option[Int])
Generic[Result].from(hChain(4, f1 :: f2 :: f3 :: HNil)) ==
  Result(Some(4),Some("4"),Some(4),Some(2))





