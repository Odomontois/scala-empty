import scalaz.syntax.std.boolean._

List(1, 2, 3).nonEmpty.when(println("ok"))
