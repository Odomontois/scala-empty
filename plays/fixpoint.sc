def collatz(n: Int): Int =
  if (n == 1) n
  else if (n % 2 == 0) collatz(n / 2)
  else collatz(3 * n + 1)

collatz(3)

def collatzR(recur: ⇒ Int ⇒ Int)(n: Int): Int =
  if (n == 1) n
  else if (n % 2 == 0) recur(n / 2)
  else recur(3 * n + 1)

def report[A, B](recur: ⇒ A ⇒ B)(x: A): B = {
  println(x)
  recur(x)
}

implicit class ComposeLazy[A, B](f: (⇒ A) ⇒ B){
  def andThenLz[C](g: (⇒ B) ⇒ C): (⇒ A) ⇒ C = x ⇒ g(f(x))
}



def fix[A](f: (=>A) => A): A = f(fix(f))

collatz(100)

fix(collatzR _ andThenLz report[Int, Int])(27)




