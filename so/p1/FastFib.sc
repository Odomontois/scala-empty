def fastFib(x: Long): Long = {
  def go(x :(Long, Long), n: Int): Long = if(n == 1) x else x match  {
    case (a, b) =>
      //applying the fast Fibonacci algorithm
      val c = a * (b * 2 - a)
      val d = a * a + b * b

      if (c + d % 2 == 0) (c, d) else (d, c + d)
  }
  go((x - 1, x - 2))._1
}