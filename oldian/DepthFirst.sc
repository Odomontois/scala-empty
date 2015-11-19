import scala.collection.{LinearSeqLike, LinearSeq}
import scala.language.higherKinds
import scalaz._
import scalaz.syntax.monad._
import scalaz.syntax.writer._
import scalaz.std.option._
import Semigroup.firstTaggedSemigroup
import scalaz.syntax.monoid._
import scalaz.syntax.std.option._
import scalaz.WriterT._
import scalaz.std.tuple._
type Build[E, M[_], T[_]] = M[E] ⇒ M[T[E]]
class DepthFirst[E, M[_] : Monad, T[X] <: LinearSeqLike[X, T[X]]](build: Build[E, M, T]) {
  import StreamT._

  def next(current: M[T[E]], stack: List[T[E]]): StreamT[M, E] = StreamT(current flatMap {
    case head +: tail ⇒ Yield(head, next(build(current >| head), tail :: stack))
    case _ ⇒ stack match {
      case prev :: upper ⇒ Skip(next(prev, upper))
      case _ ⇒ Done
    }
  })
  def run(start: E): M[Stream[E]] = produce(start, Nil).toStream
}
class DFSearchFirst[E, M[_] : Monad, T[X] <: LinearSeqLike[X, T[X]] : PlusEmpty, S: Monoid]
(generate: Build[(E, S), M, T], mark: E ⇒ S, valid: E ⇒ Boolean) {
  type Solution = Option[FirstOf[E]]
  type Writee = (S, Solution)
  type Search[X] = WriterT[M, Writee, X]
  val WTrans = MonadTrans[WriterT[?[_], Writee, ?]]
  val WTell = MonadTell[WriterT[M, ?, ?], Writee]
  val initial = mzero[S]
  val tempty = PlusEmpty[T].empty[E]

  def success(elem: E): Writee = (initial, Tag(elem).some)

  def build[X](state: Search[X], elem: E): Search[T[E]] = {
    if (valid(elem)) state :++> success(elem) >| tempty
    else {
      val prev = for {
        (state, sol) ← state.written

      } yield ???
      ???
    }
  }
}

