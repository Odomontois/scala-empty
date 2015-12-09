/**
  * Author: Oleg Nizhnik
  * Date  : 30.11.2015
  * Time  : 9:21
  */
package so

import play.api.libs._
import json._
import org.joda.time.DateTime
import java.util.Date
import play.api.libs.json.Json

import scala.annotation.StaticAnnotation

import shapeless.{`::` => :#:, _}
//import shapeless._
import poly._

object SReads extends LabelledTypeClassCompanion[Reads] {
  object typeClass extends LabelledTypeClass[Reads] {

    def emptyProduct: Reads[HNil] = Reads(_ => JsSuccess(HNil))

    def product[F, T <: HList](name: String, FHead: Reads[F], FTail: Reads[T]) = Reads[F :#: T] {
      case obj@JsObject(fields) =>
        for {
          head <- (obj \ name).validate(FHead)
          tail <- FTail.reads(obj - name)
        } yield head :: tail

      case _ => JsError("Json object required")
    }

    def project[F, G](instance: => Reads[G], to: F => G, from: G => F) = Reads[F](instance.map(from).reads)

    def emptyCoproduct: Reads[CNil] = Reads[CNil](_ => JsError("CNil object not available"))

    def coproduct[L, R <: Coproduct](
                                      name: String,
                                      cl: => Reads[L],
                                      cr: => Reads[R]) = Reads[L :+: R] { js =>

      js match {
        case js@JsString(n) if n == name => cl.reads(js).map(Inl.apply)
        case js@_ => cr.reads(js).map(Inr.apply)
      }
    }
  }
}

case class Custom(x: Int)

class Key(name: String) extends StaticAnnotation

case class TrialMember(
                        @Key("_id") val id: String,
                        val weeks: String,
                        val `type`: Option[String] = Some("email"),
                        val updatedDate: Date = DateTime.now.toDate
                      )

object Custom {
  implicit def customReads: Reads[Custom] = SReads.deriveInstance
}

object optionFormats {
  def noneReads[T]: Reads[Option[T]] = Reads(Function.const(JsSuccess(None)))

  implicit def optFormat[T](implicit w: Writes[T], r: Reads[T]) =
    Format[Option[T]](
      r.map[Option[T]](Some(_)).orElse(noneReads),
      Writes(_.fold[JsValue](JsNull)(w.writes)))
}

object TrialMember {

  import optionFormats._

  implicit def trialMemberReads: Reads[TrialMember] = SReads.deriveInstance
  val jstr =
    """
      |{
      | "id" : "1a",
      | "weeks" : "yappa",
      | "updatedDate" : "2015-12-01T00:00:00"
      |}
    """.stripMargin

  def main(args: Array[String]) {
    println(Json.parse(jstr).validate[TrialMember])
  }
}
