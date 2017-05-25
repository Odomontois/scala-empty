sealed trait Iter[+A, +B]
case class Return[+B](res: B) extends Iter[Nothing, B]
case class Recur[+A](arg: A) extends Iter[A, Nothing]

def fix[A, B](f: A => Iter[A, B]): A => B = x => {
  def go(x: A): B = f(x) match {
    case Return(res) => res
    case Recur(arg) => go(arg)
  }
  go(x)
}

def sum(from: Long, to: Long, step: Long, acc: Long) =
  if (from > to) Return(acc)
  else Recur(from + step, to, step, acc + from)

val sumf = fix((sum _).tupled)

sumf(1, 1000000, 3, 0)