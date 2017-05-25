package so

import cats.{Applicative, Eval, Monad, MonadFilter, Traverse}

import scala.annotation.tailrec


/**
  * User: Oleg
  * Date: 31-Dec-16
  * Time: 15:13
  */
object CatsStateMonad {

  import cats.data.{State, StateT}
  import cats.{MonadState, Foldable, Functor, FunctorFilter}
  import cats.instances.option._
  import cats.syntax.foldable._
  import cats.syntax.functor._
  import cats.syntax.functorFilter._
  import monocle.macros.Lenses

  @Lenses
  case class Pos(x: Int, y: Int)

  sealed abstract class Dir(val cmd: Pos ⇒ Pos)

  case object South extends Dir(Pos.y.modify(_ - 1))
  case object North extends Dir(Pos.y.modify(_ + 1))
  case object East extends Dir(Pos.x.modify(_ + 1))
  case object West extends Dir(Pos.x.modify(_ - 1))

  @Lenses
  case class PosAndDir(pos: Pos, dir: Dir)

  val clockwise = Vector(North, East, South, West)
  val left: Map[Dir, Dir] = clockwise.zip(clockwise.tail :+ clockwise.head).toMap
  val right: Map[Dir, Dir] = left.map(_.swap)

  sealed abstract class Instruction(val cmd: PosAndDir ⇒ PosAndDir)
  case object TurnLeft extends Instruction(PosAndDir.dir.modify(left))
  case object TurnRight extends Instruction(PosAndDir.dir.modify(right))
  case object Walk extends Instruction(pd ⇒ PosAndDir.pos.modify(pd.dir.cmd)(pd))

  def runInstructions[F[_] : Foldable : Functor](instructions: F[Instruction])(start: PosAndDir): PosAndDir =
    instructions.map(i => State.modify(i.cmd)).sequence_.runS(start).value

  type Walk[x] = StateT[Option, PosAndDir, x]
  val stateMonad = MonadState[Walk, PosAndDir]

  import stateMonad._

  implicit class FunctorWithFilter[F[_] : FunctorFilter, A](fa: F[A]) {
    def withFilter(f: A ⇒ Boolean) = fa.filter(f)
  }

  def update2(i: Instruction): StateT[Option, PosAndDir, Pos] =
    for (pad ← get if i == Walk) yield pad.pos


  import cats.syntax.apply._
  import cats.syntax.applicative._

  implicit val seqInstance = new MonadFilter[Seq] with Traverse[Seq] {
    def traverse[G[_] : Applicative, A, B](fa: Seq[A])(f: (A) ⇒ G[B]): G[Seq[B]] =
      fa match {
        case head +: tail ⇒ f(head).map2(traverse(tail)(f))(_ +: _)
        case _empty ⇒ Seq.empty[B].pure[G]
      }

    def foldLeft[A, B](fa: Seq[A], b: B)(f: (B, A) ⇒ B): B = fa.foldLeft(b)(f)

    def foldRight[A, B](fa: Seq[A], lb: Eval[B])(f: (A, Eval[B]) ⇒ Eval[B]): Eval[B] =
      fa match {
        case head +: tail ⇒ f(head, foldRight(tail, lb)(f))
        case _empty ⇒ lb
      }

    def pure[A](x: A): Seq[A] = Seq(x)

    def empty[A]: Seq[A] = Seq.empty[A]

    def flatMap[A, B](fa: Seq[A])(f: (A) ⇒ Seq[B]): Seq[B] = fa.flatMap(f)

    def tailRecM[A, B](a: A)(f: (A) ⇒ Seq[Either[A, B]]): Seq[B] = {
      @tailrec def go(seq: Seq[Either[A, B]]): Seq[B] =
        if (seq.contains((_: Either[A, B]).isLeft))
          go(seq.flatMap {
            case Left(a) ⇒ f(a)
            case b ⇒ Seq(b)
          }) else seq.collect { case Right(b) ⇒ b }

      go(Seq(Left(a)))
    }

        override def mapFilter[A, B](fa: Seq[A])(f: (A) ⇒ Option[B]): Seq[B] =
          fa.flatMap(f(_).toSeq)

  }

}
