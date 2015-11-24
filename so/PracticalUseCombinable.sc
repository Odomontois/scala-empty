trait Combine[A, B] {
  def apply(l: List[A]): B
}

implicit object CombineStrings extends Combine[String, String] {
  def apply(l: List[String]) = l.mkString
}

implicit object CombineInts extends Combine[Int, Int] {
  def apply(l: List[Int]) = l.sum
}

implicit object CombinableIntAsString extends Combine[Int, String] {
  def apply(l: List[Int]) = l.mkString(",")
}

trait Combinable[A] {
  def join[B](l: List[A])(implicit combine: Combine[A, B]): B =
    combine(l)
}

val a = new Combinable[String] {}
val b = new Combinable[Int] {}

a.join[String](List("a", "b", "c"))
b.join[Int](List(1, 2, 3))
b.join[String](List(1, 2, 3))

a.join[Int](List("a", "b", "c"))


