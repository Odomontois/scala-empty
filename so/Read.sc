trait Read[T] {
  def fromString(s: String): T
}

implicit object readBigInt extends Read[BigInt] {
  def fromString(s: String): BigInt = BigInt(s)
}

implicit object readDouble extends Read[Double] {
  def fromString(s: String): Double = s.toDouble
}

trait ReadSeq[T] {
  def fromStrings(ss: Seq[String]): T
}

import shapeless._

implicit object readHNil extends ReadSeq[HNil] {
  def fromStrings(ss: Seq[String]) = HNil
}

implicit def readHList[X, XS <: HList](implicit head: Read[X], tail: ReadSeq[XS]) =
  new ReadSeq[X :: XS] {
    def fromStrings(ss: Seq[String]) = ss match {
      case s +: rest => (head fromString s) :: (tail fromStrings rest)
    }
  }

implicit def readCase[C, L <: HList](implicit gen: Generic.Aux[C, L], read: ReadSeq[L]) =
  new ReadSeq[C] {
    def fromStrings(ss: Seq[String]) = gen.from(read.fromStrings(ss))
  }

def fromStringSeq[T](ss: Seq[String])(implicit read: ReadSeq[T]) = read.fromStrings(ss)

case class Location(id: BigInt, lat: Double, lng: Double)

val repr = List("87222", "42.9912987", "-93.9557953")

fromStringSeq[Location](repr) == Location(BigInt("87222"), 42.9912987, 93.9557953)

