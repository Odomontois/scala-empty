import org.apache.spark.rdd.RDD
List(
  Array("Mark", "2000", "2002", "John#Andrew", "Erik"),
  Array("John", "2001", "2003", "Mark#Andrew", "Erik"),
  Array("Andrew", "1999", "2001", "Mark#John", "Erik"),
  Array("Erik", "1996", "1998", "", "Mark#John#Andrew")
)

val x = List(Array("Mark", "2000", "2002"), Array("John", "2001", "2003"), Array("Andrew", "1999", "2001"), Array("Erik", "1996", "1998"))

val y = List(Array("Steve", "2000", "2005"))

def overlaps(periods: List[Array[String]]): List[Array[String]] = {
  val all = periods.map(_.head).toSet
  val meet = periods
    .flatMap { case Array(name, enter, leave) => List(Right(name, enter), Left(name, leave)) }
    .sortBy {
      case Right((name, enter)) => (enter, -1)
      case Left((name, leave)) => (leave, 1)
    }.foldLeft((Set.empty[String], List.empty[(String, Set[String])])) { (state, event) =>
    val (having, accum) = state
    event match {
      case Right((name, enter)) =>
        val newAccum = if (having.isEmpty) accum else (name, having) :: accum
        (having + name, newAccum)
      case Left((name, leave)) =>
        val removed = having - name
        val newAccum = if (removed.isEmpty) accum else (name, removed) :: accum
        (removed, newAccum)
    }
  }
    ._2.groupBy(_._1).mapValues(_.map(_._2).reduce(_ union _)).withDefaultValue(Set.empty)

  periods.map { case Array(name, enter, leave) =>
    Array(name, enter, leave, meet(name).mkString("#"), ((all - name) diff meet(name)).mkString("#"))
  }
}

import Ordering.Implicits._

def segmentOverlapping[X: Ordering](from1: X, to1: X, from2: X, to2: X): Boolean =
  (from1 <= from2 && to1 >= to2) || (from1 <= to2 && to1 >= to2)
def sparkOverlaps(periods: RDD[Array[String]]): RDD[Array[String]] = {
  periods.zip(periods).collect {
    case (Array(name1, from1, to1), Array(name2, from2, to2))
      if segmentOverlapping(from1, to1, from2, to2) => ((name1, from1, to1), Seq((name2, from2, to2)))
  }.
}