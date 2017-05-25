
import shapeless._
import shapeless.labelled.FieldType

import scala.language.postfixOps

import org.joda.time.DateTime
class Common(
              val solution: Long,
              val banner: Int
            )

class MyInfo(
              myId: String,
              time: DateTime,
              solution: Long,
              banner: Int
            ) extends Common(solution, banner)

class HistInfo(
                id: String,
                solution: Long,
                banner: Int
              ) extends Common(solution, banner)

val lgen = LabelledGeneric[Common]
lgen.to(new HistInfo("123", 12, 3))

trait Helper[T] extends (T => Array[Byte])

implicit object longHelper extends Helper[Long] {
  def apply(x: Long) = 0 to 7 map (i => (x >> (i * 8)).toByte) toArray
}

implicit object intHelper extends Helper[Int] {
  def apply(x: Int) = 0 to 3 map (i => (x >> (i * 8)).toByte) toArray
}

object serialized extends Poly1 {
  implicit def serialize[K <: Symbol, T]
  (implicit helper: Helper[T], key: Witness.Aux[K])
  = at[FieldType[K, T]](field => key.value.name -> helper(field))
}

val histInfo = new HistInfo("123", 12, 3)
def extractMap(x: Common): Map[String, Seq[Byte]] =
  lgen.to(histInfo).map(serialized).toList.toMap.mapValues(_.toSeq)
println(extractMap(histInfo))

