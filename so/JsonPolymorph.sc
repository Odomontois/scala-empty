import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.functional.syntax._

sealed trait AttributeValue
case class AttributeInt(value: Int) extends AttributeValue
case class AttributeDouble(value: Double) extends AttributeValue
case class AttributeString(value: String) extends AttributeValue

case class Attribute (name: String, value: AttributeValue)

val attributeByDatatype: PartialFunction[String, JsPath => Reads[AttributeValue]] = {
  case "integer" => _.read[Int].map(AttributeInt)
  case "double" => _.read[Double].map(AttributeDouble)
  case "string" => _.read[String].map(AttributeString)
}

implicit val attributeReads: Reads[Attribute] = (
      (JsPath \ "name").read[String] and
      (JsPath \ "fieldDataType")
      .read[String]
      .collect(ValidationError("datatype unknown"))(attributeByDatatype)
      .flatMap(_(JsPath \ "value"))
  )(Attribute.apply _)

val json =
  """
    |[
    |      {
    |        "name" : "Weather",
    |        "value" : "Cloudy",
    |        "fieldDataType" : "string"
    |      },
    |      {
    |        "name" : "pH",
    |        "value" : 7.2,
    |        "fieldDataType" : "double"
    |      },
    |      {
    |        "name" : "Quality Indicator",
    |        "value" : 3,
    |        "fieldDataType" : "integer"
    |      }
    |    ]
  """.stripMargin

Json.parse(json).validate[Seq[Attribute]]