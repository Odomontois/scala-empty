package chat

import scalaz._

import scalaz.StreamT.{Skip, Yield}
import scalaz.syntax.monad._
import monocle.macros.Lenses
import monocle.state.all._
import scalaz.Free.Trampoline
import scalaz.std.vector._

@Lenses
case class PrimeState(primes: Vector[Int], num: Int, nextDivs: Vector[Int])


object StreamTShit {

  import PrimeState.{num, primes, nextDivs}

  implicit class TrampoOps[A](val x: State[PrimeState, A]) extends AnyVal {
    def trampo: PS[A] = x.mapK { case (s, a) ⇒ Trampoline.done((s, a)) }
  }

  type PS[A] = StateT[Trampoline, PrimeState, A]
  type PrimeStream = StreamT[PS, Int]

  def start = PrimeState(primes = Vector(2), num = 1, nextDivs = Vector())


  val stateMonad = MonadState[PS, PrimeState]

  def calcPrimes: PrimeStream = {
    implicit val monadCache = stateMonad

    def checkNext(div: Int, num: Int) = {
      if (div * div > num) primes.mod(_ :+ num).trampo >| Yield(num, inc)
      else if (num % div == 0) Skip(inc).point[PS]
      else nextDivs.mod(_.tail).trampo >| Skip(nextDiv)
    }

    def nextDiv: PrimeStream = StreamT[PS, Int](
      for {
        current ← num.st.trampo
        divs ← nextDivs.st.trampo
        next ← checkNext(divs.head, current)
      } yield next
    )

    def inc: PrimeStream = StreamT[PS, Int](
      for {
        prs ← primes.st.trampo
        _ ← num.mod(_ + 2).trampo
        _ ← nextDivs.assign(prs).trampo
      } yield Skip(nextDiv)
    )

    StreamT(Yield(2, inc).point[PS])
  }

  val primes100 = calcPrimes.take(10)

  import WriterT.writerT

  type Log = Vector[(Int, Option[Int])]
  type Report[A] = WriterT[PS, Log, A]
  val report = new (PS ~> Report) {
    def apply[A](fa: PS[A]): Report[A] = writerT[PS, Log, A](
      for {
        value ← fa
        current ← num.st.trampo
        divs ← nextDivs.st.trampo
      } yield (Vector((current, divs.headOption)), value)
    )
  }

  def main(args: Array[String]): Unit = {
    val primes100 = calcPrimes.take(100)
//    primes100.toStream.run(start).run._2.foreach(println)
    primes100.trans(report).toStream.run.run(start).run._2._1.foreach(println)
  }

}
