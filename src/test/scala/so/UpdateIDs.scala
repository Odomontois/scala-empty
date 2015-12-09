/**
  * Author: Oleg Nizhnik
  * Date  : 23.11.2015
  * Time  : 11:24
  */
package so

import org.apache.spark.rdd.RDD

import Numeric.Implicits._
import scala.reflect.ClassTag

case class Foo(id: Int)
abstract class UpdateIDs[T: ClassTag, Id: Numeric : ClassTag] extends Serializable {
  def getId(elem: T): Id
  def setId(elem: T, id: Id): T
  def shouldChange(elem: T): Boolean
  val Id = implicitly[Numeric[Id]]

  def apply(xs: RDD[T]): RDD[T] = {
    val next = xs.map(getId).max + Id.one
    val counts: Seq[(Int, Int)] = xs.mapPartitionsWithIndex { (idx, elems) =>
      Iterator.single(idx, elems.count(shouldChange))
    }.collect.view
    val starts = counts.map(_._2).map(Id.fromInt).scanLeft(next)(_ + _)
    val startMapLocal = counts.zip(starts).map { case ((idx, _), start) => (idx, start) }.toMap
    val startMap = xs.context.broadcast(startMapLocal)

    xs.mapPartitionsWithIndex { case (idx, elems) =>
      elems.scanLeft((List.empty[T], startMap.value(idx))) { (pair, elem) =>
        pair match {
          case (_, counter) if shouldChange(elem) => (List(elem, setId(elem, counter)), counter + Id.one)
          case (_, counter) => (List(elem), counter)
        }
      }.flatMap { _._1 }
    }
  }
}

object fooUpdateId extends UpdateIDs[Foo, Int] {
  def getId(foo: Foo) = foo.id
  def setId(foo: Foo, id: Int) = foo.copy(id = id)
  def shouldChange(foo: Foo) = foo.id % 2 == 1
}

object UpdateIDs{
  def main(args: Array[String]) {
    import Spark._

    val a = sc.parallelize(1 to 3 map Foo)

    println(fooUpdateId(a).collect.toSeq)
  }
}

