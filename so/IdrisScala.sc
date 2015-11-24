import shapeless._
import shapeless.syntax.singleton._

import scala.reflect.runtime.universe._
trait Type1[A] {type T}
object Type1 {
  type Of[T1] = {type λ[a] = Type1[a] {type T = T1}}
}
val wTrue = Witness(true)
type True = wTrue.T
val wFalse = Witness(false)
type False = wFalse.T
implicit val Type1True: Type1[True] = new Type1[True] {type T = Int}
implicit val Type1False: Type1[False] = new Type1[False] {type T = String}

trait TypeName[T] {
  def name: String
}

implicit val stringName = new TypeName[Int] {def name = "Int"}
implicit val intName = new TypeName[String] {def name = "String"}

def typeName[N](implicit name: TypeName[N]) = name.name

def stringOrInt[A <: Boolean](implicit typ: Type1[A]): Type1[A] = typ

def stringOrIntName[A <: Boolean: Type1.Of[N]#λ, N: TypeName](w: Witness.Aux[A]) = typeName[N]
val (t1, t2) = (stringOrInt[True], stringOrInt[False])
stringOrIntName(wTrue)

