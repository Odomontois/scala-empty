/**
  * User: Oleg
  * Date: 06-Jan-17
  * Time: 23:11
  */
package object chat {
  implicit class BiFunctionAsForeachAction[K, V](f: (K, V) ⇒ Unit) extends ForeachAction[K, V]{
    def apply(key: K, value: V): Unit = f(key, value)
  }
}
