import shapeless._
import syntax.std.tuple._

val pair: (Option[String], Option[String]) = (Some("a"), None)

class Default[T](val value: T)

implicit object defaultString extends Default[String]("")

object extract extends Poly1 {
  implicit def withDefault[T](implicit default: Default[T]) =
    at[Option[T]](_ getOrElse default.value)
}

pair.map(extract)