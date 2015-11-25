/**
  * Author: Oleg Nizhnik
  * Date  : 13.11.2015
  * Time  : 14:15
  */
package odo
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SQLContext

object Spark {
  val conf = new SparkConf().setAppName("odo").setMaster("local[2]")
  val cs = new SparkContext(conf)
  val sc = new SQLContext(cs)
}
