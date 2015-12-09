implicit class StreamChopOps[T](xs: Stream[T]) {
  def chopBy[U](f: T => U): Stream[Stream[T]] = xs match {
    case x #:: _ => xs.takeWhile(_ == x) #:: xs.dropWhile(_ == x).chopBy(f)
    case _ => Stream.empty
  }
}

List().view.reverse

Stream(1, 1, 2, 2, 3, 1, 1).chopBy(identity).map(_.force).force

Iterator.fill(10000000)(Iterator(1, 1, 2)).flatten
  .toStream.chopBy(identity)
  .map(xs => xs.sum * xs.size).sum

//Iterator(1,2,3).span()