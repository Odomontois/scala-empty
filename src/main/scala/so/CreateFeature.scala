/**
  * Author: Oleg Nizhnik
  * Date  : 23.11.2015
  * Time  : 12:45
  */
package so
import odo.Spark._

object CreateFeature {
  def main(args: Array[String]) {
    import sc.implicits._
    
    val cats = sc.read.json("D:\\prog\\projects\\sbt\\empty\\src\\main\\resources\\cats.json")
    val catMaps = cats.rdd
      .groupBy(_.getAs[Long]("userId"))
      .map { case (id, rows) => id -> rows
        .map { row => row.getAs[String]("category") -> row.getAs[Long]("frequency") }
        .toMap
      }
//    val catNames = cs.broadcast(catMaps.map(_._2.keySet).reduce(_ union _).toArray.sorted)

    val catNames = cs.broadcast(1 to 10 map {n => s"cat$n"} toArray)
    val catArrays = catMaps
      .map { case (id, catMap) => id -> catNames.value.map(catMap.getOrElse(_, 0L)) }
      .toDF("userId", "feature")

    catArrays.show()

  }
}
