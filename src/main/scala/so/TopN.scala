package so

import scala.collection.immutable.TreeSet

object TopN {
  def apply[A: Ordering](n: Int, q: TreeSet[A]) = new TopN(n, q.drop(q.size - n))

  def empty[A: Ordering](n: Int) = new TopN[A](n, TreeSet.empty )
}

class TopN[A: Ordering](n: Int, val q: TreeSet[A]) extends Serializable{
  def +(a: A) = TopN(n, q + a)

  def ++(that: TopN[A]) = TopN(n, q ++ that.q)
}