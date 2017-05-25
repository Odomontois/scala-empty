package cheryl

import shapeless._, ops.hlist._

sealed class FirstIs[M, T]
object FirstIs {
  implicit def firstIs[M, D]: FirstIs[M, (M, D)] = new FirstIs
}

object Cheryl{
  def main(args: Array[String]): Unit = {
    LiftAll[FirstIs[Int, ?], HNil].instances
  }
}