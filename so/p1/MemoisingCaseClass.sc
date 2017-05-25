import scala.collection.mutable

class Doubler private(a: Int) {
  lazy val double = {
    println("calculated")
    a * 2
  }
}

object Doubler{
  val cache = mutable.WeakHashMap.empty[Int, Doubler]
  def apply(a: Int): Doubler = cache.getOrElseUpdate(a, new Doubler(a))
}

Doubler(1).double   //calculated

Doubler(5).double   //calculated

Doubler(1).double   //most probably not calculated







