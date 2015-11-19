import scalaz._
import scalaz.syntax.tree._
import scalaz.std.function._
import scalaz.syntax.arrow._
import scalaz.std.string._


object subr {
  case class Subroutine[-A, +B](hier: Seq[Tree[String]], run: A => B) {
    def named(name: String) = Subroutine(Seq(name.node(hier: _*)), run)

    def printHier = hier.map(_.drawTree).mkString("\n" + "V" * 15 + "\n")
  }

  object Subroutine {
    def named[A, B](tag: String)(run: A => B) = Subroutine(Seq(tag.leaf), run)

    implicit def anon[A, B](run: A => B) = Subroutine(Seq.empty, run)


    implicit object subroutineArrow extends Arrow[Subroutine] {
      def arr[A, B](f: (A) => B): Subroutine[A, B] = anon(f)

      def first[A, B, C](f: Subroutine[A, B]): Subroutine[(A, C), (B, C)] =
        Subroutine(f.hier, f.run.first[C]).named("$1->")

      override def second[A, B, C](f: Subroutine[A, B]): Subroutine[(C, A), (C, B)] =
        Subroutine(f.hier, f.run.second[C]).named("$2->")

      def id[A]: Subroutine[A, A] = anon(identity)

      def compose[A, B, C](f: Subroutine[B, C], g: Subroutine[A, B]): Subroutine[A, C] =
        Subroutine(g.hier ++ f.hier, f.run compose g.run)
    }
  }
}

import subr._
import subr.Subroutine._

val square = { (x: Double) => x * x } named "square"
val sqrt = math.sqrt _ named "sqrt"

val sum = Subroutine.named[(Double, Double), Double]("sum") { case (x, y) => x + y }


val abs = ((square *** square) >>> sum >>> sqrt) named "abs"


abs.run(3, 4)

println(abs.printHier)
def pack22[X] = subr.Subroutine.anon[(X, X, X, X), ((X, X), (X, X))] { case (a, b, c, d) => ((a, b), (c, d)) }

val abs4 = ((abs *** abs) >>> abs <<< pack22[Double]) named "abs4"

abs4.run(15, 20, 36, 48)

abs4.printHier

