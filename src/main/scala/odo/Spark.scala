/**
  * Author: Oleg Nizhnik
  * Date  : 13.11.2015
  * Time  : 14:15
  */
package odo
import org.apache.spark.{SparkContext, SparkConf}

object Spark {
  val conf = new SparkConf().setAppName("odo").setMaster("local[2]")
  val sc = new SparkContext(conf)
}
