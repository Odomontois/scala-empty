//package so.spark
//
///**
//  * User: Oleg
//  * Date: 24-Nov-15
//  * Time: 22:12
//  */
//object SparkSomething {
//  val config = new SparkConf().setAppName("odo").setMaster("local[2]").set("spark.driver.host", "localhost")
//  val cs = new SparkContext(config)
//  val sc = new SQLContext(cs)
//
//
//  def main(args: Array[String]) {
//    val df = sc.read.json("C:\\Users\\Oleg\\Documents\\oldian\\cities.json")
//    df.show(30)
//  }
//}
