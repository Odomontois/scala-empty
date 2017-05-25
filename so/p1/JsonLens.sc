import play.api.libs.json._
import monocle._
import monocle.function._
import monocle.std.map._

import scalaz.{\/, Applicative}
type =?>[-X, +Y] = PartialFunction[X, Y]
val asJsObj: JsValue =?> JsObject = {
  case x: JsObject => x
}
val asJsNumber: JsValue =?> JsNumber = {
  case x: JsNumber => x
}
val jsObject = Prism(asJsObj.lift)(identity)
val jsNumber = Prism(asJsNumber.lift)(identity)
val jsNumInt = Iso[JsNumber, Int](x => x.value.toInt)(x => JsNumber(BigDecimal(x)))
def mapImmutable[K, V] = Iso[collection.Map[K, V], Map[K, V]] {
  case m: Map[K, V] => m
  case m => m.toMap
}(identity)

val jsObjMap = Iso[JsObject, collection.Map[String, JsValue]](_.value)(JsObject(_))

implicit class POptStrictComposition[S, T, A, B](self: POptional[S, T, A, B]) {
  def sComposePrism[C, D](other: PPrism[A, B, C, D]) = new POptional[S, T, C, D] {
    def getOrModify(s: S): T \/ C =
      self.getOrModify(s).flatMap(a => other.getOrModify(a).bimap(self.set(_)(s), identity))

    def set(d: D): S => T =
      self.set(other.reverseGet(d))

    def getOption(s: S): Option[C] =
      self.getOption(s) flatMap other.getOption

    def modifyF[F[_] : Applicative](f: C => F[D])(s: S): F[T] =
      self.modifyF(other.modifyF(f))(s)

    def modify(f: C => D): S => T =
      self.modify(other.modify(f))
  }

  def ^!<-?[C, D](o: PPrism[A, B, C, D]) = sComposePrism(o)

  def sComposeIso[C, D](other: PIso[A, B, C, D]) = sComposePrism(other.asPrism)

  def ^!<->[C, D](o: PIso[A, B, C, D]) = sComposeIso(o)
}

val ageJs = jsObject ^<-> jsObjMap ^<-> mapImmutable ^|-? index("age")
val age = ageJs  ^<-? jsNumber ^<-> jsNumInt
val x: JsObject = JsObject(Seq.empty)
ageJs.set(JsNumber(10))(x)

//import monocle.std.map._
val mmm = Map.empty[Int, Option[Int]]

//index[Map[Int, Option[Int]], Int, Option[Int]](1).set(Some(2))(m)

