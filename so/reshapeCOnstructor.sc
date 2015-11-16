import shapeless._
import syntax.singleton._
import ops.hlist._

class PartialConstructor[C, Default <: HList, Repr <: HList]
(default: Default)
(implicit lgen: LabelledGeneric.Aux[C, Repr]) {

  def apply[Args <: HList, Full <: HList]
  (args: Args)
  (implicit prepend: Prepend.Aux[Default, Args, Full],
   align: Align[Full, Repr]): C =

    lgen.from(align(default ++ args))
}

class Reshaper[C]() {

  def apply[Default <: HList, Repr <: HList]
  (default: Default)
  (implicit lgen: LabelledGeneric.Aux[C, Repr]) =

    new PartialConstructor[C, Default, Repr](default)
}

def reshape[C] = new Reshaper[C]

case class MyClass(a: Double, b: String, c: Int)

val newConstructor = reshape[MyClass]('b ->> "bValue" :: HNil)

newConstructor('a ->> 3.1 :: 'c ->> 4 :: HNil)