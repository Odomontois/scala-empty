//package so
//
//import java.util.NoSuchElementException
//
//import scala.collection.generic.{CanBuild, CanBuildFrom}
//import scala.collection.mutable.ListBuffer
//import scala.collection.{LinearSeqLike, immutable, mutable, IterableLike}
//
///**
//  * User: Oleg
//  * Date: 28-Feb-16
//  * Time: 16:59
//  */
//case class Unfold[S, +E](f: S => Option[(S, E)])(initial: S) extends immutable.Iterable[E] with IterableLike[E, Unfold[S, E]] with LinearSeqLike[E, Unfold[S, E]] {
////  def iterator = new Iterator[E] {
////    var it = f(initial)
////
////    def hasNext: Boolean = it.isDefined
////
////    def next(): E = it match {
////      case None => throw new NoSuchElementException
////      case Some((s, el)) =>
////        it = f(s)
////        el
////    }
////  }
//
//
//
//  override def toStream: Stream[E] = f(initial) match {
//    case None => Stream()
//    case Some((next, x)) =>
//      println(s"extracted $next")
//      x #:: Unfold(f)(next).toStream
//  }
//
//  override protected[this] def newBuilder = throw new UnsupportedOperationException("cant not build unfold")
//}
//
//object Unfold {
//
//  implicit def unfoldStreamCBF[S, E] = new CanBuildFrom[Unfold[S, E], E, Stream[E]] {
//    def apply(): mutable.Builder[E, Stream[E]] = implicitly[CanBuild[E, Stream[E]]]
//
//    def apply(from: Unfold[S, E]): mutable.Builder[E, Stream[E]] =  new mutable.Builder[E, Stream[E]] {
//      def +=(elem: E): this.type = this
//
//      def result(): Stream[E] = from.toStream
//
//      def clear(): Unit = ()
//    }
//  }
//
//  def main(args: Array[String]) {
//    val collatz = Unfold[Int, Int] {
//      case 1 => None
//      case x =>
//        val n = if (x % 2 == 0) x / 2 else x * 3 + 1
//        Some(n, n)
//    } _
//
//    println(collatz(3).to[List])
//    println(collatz(27).to[Stream].force)
//  }
//}
