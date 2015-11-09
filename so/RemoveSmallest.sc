def removeMinWhileNotSorted[A: Ordering](xs: List[A]): List[A] =  if (xs == xs.sorted) xs else xs.splitAt(xs.indexOf(xs.min))  match {case (prefix, m :: postfix) => removeMinWhileNotSorted(prefix ++ postfix)}


removeMinWhileNotSorted(List(3,4,1,5,6, 2))
