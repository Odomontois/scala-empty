/**
  * Author: Oleg Nizhnik
  * Date  : 07.12.2015
  * Time  : 17:42
  */
package so
import org.apache.spark.sql.functions._
import org.apache.spark.graphx._

object CreateEdgeRdd {
  import Spark._

  case class Person(id: Long, country: String, age: Int)

val testPeople = Seq(
  Person(1, "Romania"    , 15),
  Person(2, "New Zealand", 30),
  Person(3, "Romania"    , 17),
  Person(4, "Iceland"    , 20),
  Person(5, "Romania"    , 40),
  Person(6, "Romania"    , 44),
  Person(7, "Romania"    , 45),
  Person(8, "Iceland"    , 21),
  Person(9, "Iceland"    , 22)
)

  def main(args: Array[String]) {
    val people = sqlc.createDataFrame(testPeople)
    val peopleR = people
      .withColumnRenamed("id"     , "idR")
      .withColumnRenamed("country", "countryR")
      .withColumnRenamed("age"    , "ageR")

    val relations = people.join(peopleR,
      (people("id") < peopleR("idR")) &&
        (people("country") === peopleR("countryR")) &&
        (abs(people("age") - peopleR("ageR")) < 5))

    val edges = EdgeRDD.fromEdges(relations.map(row => Edge(
      row.getAs[Long]("id"), row.getAs[Long]("idR"), ())))

    relations.show()
    edges.toLocalIterator.foreach(println)
  }
}
