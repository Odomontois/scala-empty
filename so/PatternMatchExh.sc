trait U
case object V extends U
case object W extends U
def f(x: Option[U]) = x match {
  case (Some(V)) => ""
}