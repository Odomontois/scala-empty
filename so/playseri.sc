import play.api.libs.json._

case class GoodEdit(good: Good, data: List[(String, Option[GoodText])])
case class Good(id: Long, partnumber: Option[String] = None)
case class GoodText(goodid: Long, languageid: Long, title: String, description: Option[String] = None)

object GoodWriters {
  implicit val goodWrites = new Writes[Good] {
    def writes(good: Good) = Json.obj("id" -> good.id, "partnumber" -> good.partnumber)
  }

  implicit val goodTextWrites = new Writes[GoodText] {
    def writes(goodText: GoodText) = Json.obj("goodid" -> goodText.goodid, "languageid" -> goodText.languageid, "title" -> goodText.title, "description" -> goodText.description)
  }

  implicit val GoodEditWrites = new Writes[GoodEdit] {
    def writes(goodEdit: GoodEdit) = Json.obj("good" -> Json.toJson(goodEdit.good), "data" -> Json.toJson(for ((lang, goodTextOpt) <- goodEdit.data)
      yield Json.obj(lang -> Json.toJson(goodTextOpt))))
  }
}

import GoodWriters._

val a = GoodEdit(Good(0), List())

implicit val x = Writes[Product] { x => Json.arr() }

Json.toJson(a)