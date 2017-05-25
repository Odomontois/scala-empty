
def countOs(s: String): Int = s.count(_.toLower == 'o')

val list = List("Stack", "Overflow", "Functor")
list.map(countOs)

val some = Option("Hello world")
some.map(countOs)

val none = Option.empty[String]
none.map(countOs)


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


val task = Future {
  Thread.sleep(100)
  "Long operation"
}

val countTask = task.map(countOs)

Await.result(countTask, Duration.Inf)
