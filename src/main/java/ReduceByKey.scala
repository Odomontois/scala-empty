import java.util.HashMap

import scala.collection.generic.{CanBuild, CanBuildFrom}

object Util {
  def measure(body: => Unit): Long = {
    val now = System.currentTimeMillis
    body
    val nowAfter = System.currentTimeMillis
    nowAfter - now
  }

  def measureMultiple(body: => Unit, n: Int): String = {
    val executionTimes = (1 to n).toList.map(x => {
      print(".")
      measure(body)
    })

    val avg = executionTimes.sum / executionTimes.size
    executionTimes.mkString("", "ms, ", "ms") + s" Average: ${avg}ms."
  }
}

object RandomUtil {
  val AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  val r = new java.util.Random();

  def randomString(len: Int): String = {
    val sb = new StringBuilder(len);
    for(i <- 0 to len - 1) {
      sb.append(AB.charAt(r.nextInt(AB.length())));
    }
    sb.toString();
  }

  def generateSeq(n: Int): Seq[(String, Int)] = {
    Seq.fill(n)((randomString(2), r.nextInt(100)))
  }
}

object ReduceByKeyComparison {

  def main(args: Array[String]): Unit = {
    implicit def iterableToPairedIterable[K, V](x: Iterable[(K, V)]) = { new PairedIterable(x) }

    val runs = 10
    val problemSize = 2000000

    val ss = RandomUtil.generateSeq(problemSize)


//    println("ReduceByKey :       " + Util.measureMultiple(reduceByKey(ss, (x: Int, y: Int) ⇒ x + y), runs))
//    println("ReduceByKey2:       " + Util.measureMultiple(reduceByKey2(ss, (x: Int, y: Int) ⇒ x + y), runs))
    println("ReduceByKeyPaired:  " + Util.measureMultiple(ss.reduceByKey((x: Int, y: Int) ⇒ x + y), runs))
    println("ReduceByKey3A:       " + Util.measureMultiple(reduceByKey3A(ss, (x: Int, y: Int) ⇒ x + y), runs))
    println("ReduceByKey3:       " + Util.measureMultiple(reduceByKey3(ss, (x: Int, y: Int) ⇒ x + y), runs))

    //    println("ReduceByKeyA:       " + Util.measureMultiple( reduceByKeyA( ss, (x:Int, y:Int) ⇒ x+y ), runs ))
  }

  // =============================================================================
  // Different implementations
  // =============================================================================

  def reduceByKey[A, B](s: Seq[(A, B)], fnc: (B, B) ⇒ B): Seq[(A, B)] = {
    val t = s.groupBy(x => x._1)
    val u = t.map { case (k, v) => (k, v.map(_._2).reduce(fnc)) }
    u.toSeq
  }

  def reduceByKey2[A, B](s: Seq[(A, B)], fnc: (B, B) ⇒ B): Seq[(A, B)] = {
    val r = s.foldLeft(Map[A, B]()) { (m, a) ⇒
      val k = a._1
      val v = a._2
      m.get(k) match {
        case Some(pv) ⇒ m + ((k, fnc(pv, v)))
        case None ⇒ m + ((k, v))
      }
    }
    r.toSeq
  }

  def reduceByKey3[A, B](s: Seq[(A, B)], fnc: (B, B) ⇒ B): Seq[(A, B)] = {
    var m = scala.collection.mutable.Map[A, B]()
    s.foreach { e ⇒
      val k = e._1
      val v = e._2
      m.get(k) match {
        case Some(pv) ⇒ m(k) = fnc(pv, v)
        case None ⇒ m(k) = v
      }
    }
    m.toSeq
  }

  def reduceByKey3A[A, B](s: Seq[(A, B)], fnc: (B, B) ⇒ B): Seq[(A, B)] = {
    val m = scala.collection.mutable.Map[A, B]()
    s.foreach { case (k,v) ⇒
      m.get(k) match {
        case Some(pv) ⇒ m(k) = fnc(pv, v)
        case None ⇒ m(k) = v
      }
    }
    m.toSeq
  }

  /**
    * Method code from [[http://ideone.com/dyrkYM]]
    * All rights to Muhammad-Ali A'rabi according to [[https://issues.scala-lang.org/browse/SI-9064]]
    */
  def reduceByKeyA[A, B](s: Seq[(A, B)], fnc: (B, B) ⇒ B): Map[A, B] = {
    s.groupBy(_._1).map(l => (l._1, l._2.map(_._2).reduce(fnc)))
  }

  /**
    * Method code from [[http://ideone.com/dyrkYM]]
    * All rights to Muhammad-Ali A'rabi according to [[https://issues.scala-lang.org/browse/SI-9064]]
    */
  class PairedIterable[K, V](x: Iterable[(K, V)]) {
    def reduceByKey(func: (V, V) => V) = {
      val map = new HashMap[K, V]
      x.foreach { pair =>
        val old = map.get(pair._1)
        map.put(pair._1, if (old == null) pair._2 else func(old, pair._2))
      }
      map
    }
  }
}