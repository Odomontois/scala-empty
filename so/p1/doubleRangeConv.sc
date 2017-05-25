object Primes {
  val primes = 2l #:: Stream.from(3, 2).map(_.toLong).filter(isPrime)

  val isPrime: Long => Boolean =
    n => primes.takeWhile(p => p * p <= n).forall(n % _ != 0)

  override def toString = primes.take(30).force.toString
}

Primes.primes.take(30).force
