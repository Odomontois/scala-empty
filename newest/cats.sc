import cats.{Eq, Functor}
import cats.instances.list._
import cats.instances.either._
import cats.instances.function._
import cats.instances.int._
import cats.instances.bigInt._


def divAll[F[_], A](x: F[A], num: A)(implicit functor: Functor[F],
                                              integral: Integral[A],
                                              eq: Eq[A]): F[Option[A]] =
  functor.map(x) {
    a =>
      if (eq.eqv(a, integral.zero)) None
      else Some(integral.quot(num, a))
  }


divAll((-3 to 3).toList, 10)
divAll(Right(BigInt(99)): Either[String, BigInt], BigInt(777))
divAll(Left("Stack"): Either[String, BigInt], BigInt(1000))


import cats.syntax.functor._
import cats.syntax.option._
import cats.syntax.eq._
import cats.syntax.either._
import Integral.Implicits._


def zero[A](implicit F: Integral[A]) = F.zero

def divAllS[F[_] : Functor, A: Integral : Eq](x: F[A], num: A): F[Option[A]] =
  x.map(a => if (a === zero) none else (num / a).some)


divAllS((-3 to 3).toList, 10)
divAllS(Either.right(BigInt(99)), BigInt(777))
divAllS(Either.left("Stack"), BigInt(1000))