//package so
//
///**
//  * User: Oleg
//  * Date: 28-Feb-16
//  * Time: 13:12
//  */
//object CollectValues {
//
//  import so.spark.Spark._
//
//  def main(args: Array[String]) {
//    val input = Seq(
//      (4048L, Array(("a", 3.0), ("b", 9.0), ("c", 14.0)).toIterable),
//      (4049L, Array(("a", 2.0), ("c", 14.0)).toIterable),
//      (4050L, Array(("b", 2.0), ("d", 10.0)).toIterable)
//    )
//
//
//    val rdd = sc.parallelize(input)
//
//
//val named = rdd.flatMap { case (key, values) => values.map { case (name, value) => (name, (key, value)) } }
//
//val nameMap = named.map(_._1).distinct().zipWithUniqueId()
//
//val indexed = named.join(nameMap).map { case (name, ((key, value), index)) => (key, (index, value)) }.groupByKey
//
//
//    indexed.foreach(println)
//  }
//}
