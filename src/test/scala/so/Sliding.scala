//package so
//
///**
//  * User: Oleg
//  * Date: 11-Dec-15
//  * Time: 19:55
//  */
//object Sliding {
//  import so.spark.Spark._
//
//  def main(args: Array[String]) {
//    import org.apache.spark.mllib.rdd.RDDFunctions._
//
//    val str = "abcdabcd"
//
//    val rdd = sc.parallelize(str)
//
//    rdd.sliding(2).map(_.mkString).toLocalIterator.foreach(println)
//
//  }
//}
