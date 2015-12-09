/**
  * Author: Oleg Nizhnik
  * Date  : 08.12.2015
  * Time  : 12:28
  */
package so
object SubsetsManipulations {
  val testData = Array((Vector((5, 2)), 1), (Vector((1, 1)), 2), (Vector((1, 1), (5, 2)), 2))

  implicit class SubSetsOps[T](val elems: Seq[T]) extends AnyVal {
    def subsets: Vector[Seq[T]] = elems match {
      case Seq() => Vector(elems)
      case elem +: rest => {
        val recur = rest.subsets
        recur ++ recur.map(elem +: _)
      }
    }
  }

  import Spark._
  def main(args: Array[String]) {
    val curRdd = sc.parallelize(testData)

    val result = curRdd
      .flatMap { case (keys, count) => keys.subsets.tail.map(_ -> count) }
      .reduceByKey(_ + _)

    result.foreach(println)

  }
}
