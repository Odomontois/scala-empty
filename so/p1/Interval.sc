import org.joda.time.DateTime


case class CVT[Id](taskIds: Id, begin: DateTime, end: DateTime)

val cvt1: CVT[Int] = CVT(3, new DateTime(2015, 1, 1, 1, 0), new DateTime(2015, 1, 1, 20, 0))
val cvt2: CVT[Int] = CVT(1, new DateTime(2015, 1, 1, 2, 0), new DateTime(2015, 1, 1, 3, 0))
val cvt3: CVT[Int] = CVT(1, new DateTime(2015, 1, 1, 4, 0), new DateTime(2015, 1, 1, 6, 0))
val cvt4: CVT[Int] = CVT(2, new DateTime(2015, 1, 1, 5, 0), new DateTime(2015, 1, 1, 11, 0))
val cvt5: CVT[Int] = CVT(1, new DateTime(2015, 1, 1, 7, 0), new DateTime(2015, 1, 1, 8, 0))
val cvt6: CVT[Int] = CVT(1, new DateTime(2015, 1, 1, 9, 0), new DateTime(2015, 1, 1, 10, 0))
val cvt7: CVT[Int] = CVT(1, new DateTime(2015, 1, 1, 12, 0), new DateTime(2015, 1, 1, 14, 0))
val cvt8: CVT[Int] = CVT(2, new DateTime(2015, 1, 1, 13, 0), new DateTime(2015, 1, 1, 16, 0))
val cvt9: CVT[Int] = CVT(1, new DateTime(2015, 1, 1, 15, 0), new DateTime(2015, 1, 1, 17, 0))
val cvt10: CVT[Int] = CVT(1, new DateTime(2015, 1, 1, 18, 0), new DateTime(2015, 1, 1, 19, 0))

sealed class Event
case object Enter extends Event
case object Leave extends Event

implicit val eventOrdering = Ordering.fromLessThan[Event](_ == Leave && _ == Enter)
implicit val dateTimeOrdering = Ordering.fromLessThan[DateTime](_ isBefore _)

val combinedTasks: List[CVT[Set[Int]]] = List(cvt1, cvt2, cvt3, cvt4, cvt5, cvt6, cvt7, cvt8, cvt9, cvt10)
  .flatMap { case CVT(id, begin, end) => List((id, begin, Enter), (id, end, Leave)) }
  .sortBy { case (id, time, evt) => (time, evt: Event) }
  .foldLeft((Set.empty[Int], List.empty[CVT[Set[Int]]], DateTime.now())) { (state, event) =>
    val (active, accum, last) = state
    val (id, time, evt) = event
    evt match {
      case Enter => (active + id, accum, time)
      case Leave => (active - id, CVT(active, last, time) :: accum, time)
    }
  }._2.filter(_.taskIds == Set(1,2,3)).reverse





