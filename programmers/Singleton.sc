import shapeless._
import syntax.singleton._
import scala.reflect.runtime.universe._

class Letter[C](w: Witness.Aux[C]) {
  def toUpperCase(implicit sel: ops.record.Selector[Letter.UpperCase, w.T]) =
    sel.apply(Letter.upperCase)
}

trait LetterOps {
  type UpperCase <: HList
  val upperCase: UpperCase
}

object Letter extends LetterOps {

  import ops._

  val a = Witness("a")
  val b = Witness("b")
  //...
  val A = Witness("A")
  val B = Witness("B")

  val upperCase: UpperCase =
    ("a" ->> "A") ::
      ("b" ->> "B") ::
      ("A" ->> "A") ::
      ("B" ->> "B") ::
      HNil

  type L = a.T :: b.T :: A.T :: B.T :: HNil

  def apply[C](c: Witness.Aux[C])(implicit selector: hlist.Selector[L, C]) = new Letter(c)
}


def uuu[T: TypeTag](x: T) = {
  def go(typ: Type): List[String] = typ.dealias.toString ::
    typ.baseClasses
      .map(typ.baseType)
      .filter(_ != typ)
      .flatMap(go)
      .distinct
  go(typeTag.tpe)
}



Letter(Witness("a")).toUpperCase
Letter(Witness("B"))

