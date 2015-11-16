case class Thing(num: Int)

val (targetList, rest) = xs.partition(_.num == 2)

val targetEl = targetList match {
  case x :: Nil => x
  case _ => null
}

val xs = List(Thing(1), Thing(2), Thing(3))