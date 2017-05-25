implicit class TeeSplitOp[T](data: Iterable[T]) {
  def teeSplit(count: Int): Stream[(Iterable[T], Iterable[T])] = {
    val size = data.size

    def piece(i: Int) = i * size / count

    Stream.range(0, size - 1) map { i =>
      val (prefix, rest) = data.splitAt(piece(i))
      val (test, postfix) = rest.splitAt(piece(i + 1) - piece(i))
      val train = prefix ++ postfix
      (test, train)
    }
  }
}

1 to 10 teeSplit 3 force