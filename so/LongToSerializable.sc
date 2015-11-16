class A[+T] {
  def map[U](implicit f: T => U): A[U] = ???
}

case class SerializableLong(x: Long) extends AnyVal with Serializable
object SerializableLong {
  implicit def longToSLong(x: Long) = SerializableLong(x)
  implicit def sLongToLong(x: SerializableLong) = x.x
}


import SerializableLong._
implicitly[Long => SerializableLong]
//  implicitly[A[Long] => A
val a = new A[Long]
val javaA: A[java.lang.Long] = a.map
val serA: A[SerializableLong] = a.map