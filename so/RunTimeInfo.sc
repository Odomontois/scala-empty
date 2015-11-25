
import shapeless._
import shapeless.labelled.FieldType
import shapeless.ops.hlist.{ToTraversable, Mapper}
case class Contact(phone: String, address: String)
case class User(id: Long, name: String, contact: Contact)

sealed trait TypeInfo[T] {

}

implicit case object StringField extends TypeInfo[String]

case class NumericField[N](implicit val num: Numeric[N]) extends TypeInfo[N]
implicit def numericField[N](implicit num: Numeric[N]): TypeInfo[N] = new NumericField[N]

case class ListField[E]() extends TypeInfo[List[E]]
implicit def listField[E] = new ListField[E]

sealed trait FieldDef
case class PlainField[T](value: Any, info: TypeInfo[T]) extends FieldDef
case class EmbeddedType(values: Map[Symbol, FieldDef]) extends FieldDef

sealed abstract class ClassAccessors[C] {
  def apply(c: C): Map[Symbol, FieldDef]
}

trait LowPriorClassInfo extends Poly1 {
  implicit def fieldInfo[K <: Symbol, V](implicit witness: Witness.Aux[K], info: TypeInfo[V]) =
    at[FieldType[K, V]](f => (witness.value: Symbol, PlainField(f, info)))
}

object classInfo extends LowPriorClassInfo {
  implicit def recurseInfo[K <: Symbol, V](implicit witness: Witness.Aux[K], acc: ClassAccessors[V]) =
    at[FieldType[K, V]](f => (witness.value: Symbol, EmbeddedType(acc(f))))
}

implicit def provideClassAccessors[C, L <: HList, ML <: HList]
(implicit lgen: LabelledGeneric.Aux[C, L],
 map: Mapper.Aux[classInfo.type, L, ML],
 toList: ToTraversable.Aux[ML, List, (Symbol, FieldDef)]) =
  new ClassAccessors[C] {
    def apply(c: C) = toList(map(lgen.to(c))).toMap
  }

def classAccessors[C](c: C)(implicit acc: ClassAccessors[C]) = acc(c)

object aaa {
  implicitly[TypeInfo[List[Int]]]
}

classAccessors(User(100, "Miles Sabin", Contact("+1 234 567 890", "Earth, TypeLevel Inc., 10")))


