import scalaz._
import scalaz.syntax.traverse._
import scalaz.std.list._
import Id.Id
import ReaderWriterStateT._
import scalaz.std.anyVal._

type ReadState[E, A, X] = ReaderWriterState[E, Unit, A, X]

def runStateCombinator[T, E, A](t: T, readerFactories: Seq[T => ReadState[E, A, Unit]], f: E => A): Reader[E, A] = {
  val readers = readerFactories.map(_ (t)).toList
  val stateReader = readers.sequence_[ReadState[E, A, ?], Unit](implicitly, rwstMonad[Id, E, Unit, A])
  Reader(x => stateReader.exec(x, f(x))._2)
}