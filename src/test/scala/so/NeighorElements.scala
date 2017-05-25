///**
//  * Author: Oleg Nizhnik
//  * Date  : 08.12.2015
//  * Time  : 9:46
//  */
//package so
//
//import org.apache.spark.mllib.rdd.RDDFunctions._
//
//object NeighorElements {
//  import so.spark.Spark._
//
//  def main(args: Array[String]) {
//    val seq = sc.parallelize(List(1, 3, -1, 0, 2, -4, 6)).sortBy(identity)
//
//    seq.zipWithIndex().filter(_._2 > 0).map(_._1)
//
//    val original = seq.zipWithIndex().map(_.swap)
//
//    val shifted = original.map { case (idx, v) => (idx - 1, v) }.filter(_._1 >= 0)
//
//    val diffs = original.join(shifted)
//      .sortBy(_._1, ascending = false)
//      .map { case (idx, (v1, v2)) => v2 - v1 }
//
//
//    println(diffs.collect.toSeq)
//  }
//}
