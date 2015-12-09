import scalaz._

type StringState[a] = State[String, a]
type StringReader[a] = ReaderT[StringState, String, a]
type StateChoose[a, b] = StateT[StringReader, a, b]
type StringsChoose[a] = StateChoose[Seq[String], a]
type StringsTrans[m[_], b] = StateT[m, Seq[String], b]

def ST = MonadState[StateChoose, Seq[String]]
def R = MonadReader[Reader, String]
def T = MonadTrans[StringsTrans]

def transform(action: String => Seq[String] => Seq[String]): StringsChoose[Unit] = for {
  s <- T.liftMU(R.ask)
  _ <- ST.modify(action(s))
} yield ()
def startsWithState = transform(s => _.filter(_.startsWith(s)))
def endsWithState = transform(s => _.filter(_.endsWith(s)))

def process: StringsChoose[Unit] = {
  for {
    s <- startsWithState
    e <- endsWithState
  } yield e
}
val all = Seq("AB", "BA", "ABA", "ABBA")

process.exec(all).apply("A")
