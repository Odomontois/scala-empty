import scalaz._
import scalaz.syntax.either._
def getUserParam(userId: String): Map[String, String \/ Int \/ Boolean] = {
  //do something
  Map(
    "isExist" -> true.right,
    "userDataA" -> "String".left.left,
    "userDataB" -> 1.right.left
  )
}

def doSomething(foo: String) {}

val foo = getUserParam("bar")

foo("userdata").swap.foreach(_.swap.foreach(doSomething))

sealed trait Either3[+A, +B, +C] {
  def ifFirst[T](action: A => T): Option[T] = None
  def ifSecond[T](action: B => T): Option[T] = None
  def ifThird[T](action: C => T): Option[T] = None
}

case class First[A](x: A) extends Either3[A, Nothing, Nothing] {
  override def ifFirst[T](action: A => T): Option[T] = Some(action(x))
}

case class Second[A](x: A) extends Either3[Nothing, A, Nothing] {
  override def ifSecond[T](action: A => T): Option[T] = Some(action(x))
}

case class Third[A](x: A) extends Either3[Nothing, Nothing, A] {
  override def ifThird[T](action: A => T): Option[T] = Some(action(x))
}

def getUserParam3(userId: String) = {
  //do something
  Map(
    "isExist" -> First(true),
    "userDataA" -> Second("String"),
    "userDataB" -> Third(1)
  )
}

val foo3 = getUserParam3("bar")
foo3("userdata").ifSecond(doSomething)
