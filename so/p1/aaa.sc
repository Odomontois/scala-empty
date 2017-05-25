val a = Some(List(1,1))

val u = for {
  u <- a
  List(y, _*) <- Some(u)
  List(`y`, `y`) <- Some(u)
} yield y