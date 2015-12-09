package so

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}

/**
  * User: Oleg
  * Date: 11-Nov-15
  * Time: 22:56
  */
object Spark {
  val config = new SparkConf().setAppName("odo").setMaster("local[2]").set("spark.driver.host", "localhost")
  val sc = new SparkContext(config)
  val sqlc = new SQLContext(sc)

}
