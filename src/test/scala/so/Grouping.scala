///**
//  * Author: Oleg Nizhnik
//  * Date  : 08.12.2015
//  * Time  : 11:30
//  */
//package so
//import scala.util.Random
//
//object Grouping {
//  import so.spark.Spark._
//  case class Record(id: Long, value: String, desc: String)
//
//  import org.apache.spark.sql.functions._
//  import sqlc.implicits._
//
//  val testData = for {
//    (i, j) <- List.fill(30)(Random.nextInt(5), Random.nextInt(5))
//  } yield Record(i, s"v$i$j", s"d$i$j")
//
//  def main(args: Array[String]): Unit = {
////    val df = sqlc.createDataFrame(testData)
////
////    def aggConcat(col: String) = df
////      .map(row => (row.getAs[Long]("id"), row.getAs[String](col)))
////      .aggregateByKey(Vector[String]())(_ :+ _, _ ++ _)
////
////    val result = aggConcat("value").zip(aggConcat("desc")).map {
////      case ((id, value), (_, desc)) => (id, value, desc)
////    }.toDF("id", "values", "descs")
////
////    val resultConcat = result
////      .withColumn("values", concat_ws(";", $"values"))
////      .withColumn("descs", concat_ws(";", $"descs"))
////
////
////
////    println(resultConcat.schema)
//  }
//
//}
