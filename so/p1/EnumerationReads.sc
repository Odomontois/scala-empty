import play.api.libs.json._

implicit def enumerationReads[T <: Enumeration](implicit enum: T): Reads[enum.Value] = {
  val names: Set[String] = enum.values map (_.toString)
  Reads {
    case JsString(s) => if (names contains s) JsSuccess(enum withName s)
    else JsError(s"could not find value '$s' for $enum")
    case _ => JsError("String value expected")
  }
}

implicit case object Colors extends Enumeration {
  val Red, Blue, Green = Value
}

val json =
      """
        |[ "Red", "Blue" , "Green", "Blue"]
      """.stripMargin

val badJson =
  """
    |[ "Magenta", "Blue" , "Green", "Blue"]
  """.stripMargin

Json.parse(json).validate[Seq[Colors.Value]] //res0:
// play.api.libs.json.JsResult[Seq[Colors.Value]] =
// JsSuccess(List(Red, Blue, Green, Blue),)
Json.parse(badJson).validate[Seq[Colors.Value]] //res1:
// play.api.libs.json.JsResult[Seq[Colors.Value]] =
// JsError(List(((0),List(ValidationError(List(could not find value 'Magenta' for Colors),WrappedArray())))))


