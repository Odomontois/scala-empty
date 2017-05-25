import scala.collection.immutable.{IntMap, Map, MapLike}

class OrderedMap[K, +V] private[OrderedMap](backing: Map[K, V], val order: IntMap[K], coorder: Map[K, Int], extSize: Int)
  extends Map[K, V] with MapLike[K, V, OrderedMap[K, V]] {
  def +[B1 >: V](kv: (K, B1)): OrderedMap[K, B1] = {
    val (k, v) = kv
    if (backing contains k)
      new OrderedMap(backing + kv, order, coorder, extSize)
    else new OrderedMap(backing + kv, order + (extSize -> k), coorder + (k -> extSize), extSize + 1)
  }
  def get(key: K): Option[V] = backing.get(key)

  def iterator: Iterator[(K, V)] = for (key <- order.valuesIterator) yield (key, backing(key))

  def -(key: K): OrderedMap[K, V] = if (backing contains key) {
    val index = coorder(key)
    new OrderedMap(backing - key, order - index, coorder - key, extSize)
  } else this

  override def empty: OrderedMap[K, V] = OrderedMap.empty[K, V]
}

object OrderedMap {
  def empty[K, V] = new OrderedMap[K, V](Map.empty, IntMap.empty, Map.empty, 0)

  def apply[K, V](assocs: (K, V)*): OrderedMap[K, V] = assocs.foldLeft(empty[K, V])(_ + _)
}

val a = OrderedMap(0 -> "A", 1 -> "B", 2 -> "C")
a.foreach(println)
val b = a.updated(1, "D")
b.foreach(println)

