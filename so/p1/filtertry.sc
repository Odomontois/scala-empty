import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
val conf = new SparkConf()
  .setMaster("local[2]")
  .setAppName("odo")
val sc = SparkContext.getOrCreate(conf)
import scala.util.Try
val mysc1: RDD[Array[String]] =
  sc.makeRDD(Seq(Array("", "", "NS3"), Array("")), 1)
val newRDD = for {
  x <- mysc1
  y <- x.lift(3) if y == "NS3"
} yield x

