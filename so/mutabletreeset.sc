import scala.collection.generic.{CanBuildFrom, CanBuild}
import scala.collection.immutable.TreeSet
import scala.collection.mutable
val a = mutable.TreeSet.empty[Int]
a += 1
a += 3
a.to[TreeSet]

implicitly[CanBuildFrom[Nothing, Int, TreeSet[Int]]]