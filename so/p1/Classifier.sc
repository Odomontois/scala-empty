type Tag = String
type Classifier[A] = A => Set[Tag]

import shapeless._
import shapeless.tag._
import shapeless.labelled._

trait Omit
val omit = tag[Omit]

case class Model(a: Int, b: String, c: String, d: String @@ Omit)

def aClassifier: Classifier[Int] = _ => Set("A", "a")
def bClassifier: Classifier[String] = _ => Set("B")
def cClassifier: Classifier[String] = _ => Set("C")

//def modelClassifier: Classifier[Model] = {
//  m => aClassifier(m.a) ++ bClassifier(m.b) ++ cClassifier(m.c)
//}

object classifiers extends Poly1 {
  implicit def stringClassifier[K <: Symbol](implicit witness: Witness.Aux[K]) =
    at[FieldType[K, String]](value => Set(witness.value.name.toUpperCase))

  implicit def intClassifier[K <: Symbol](implicit witness: Witness.Aux[K]) =
    at[FieldType[K, Int]](value => {
      val name = witness.value.name
      Set(name.toUpperCase, name.toLowerCase)
    })

  implicit def omitClassifier[K, T] =
    at[FieldType[K, T @@ Omit]](_ => Set.empty[String])
}

def modelClassifier: Classifier[Model] =
  m => LabelledGeneric[Model].to(m).map(classifiers).toList.reduce(_ union _)



println(modelClassifier(Model(1, "b", "c", omit("d"))))
