package so

/**
  * User: Oleg
  * Date: 11-Dec-15
  * Time: 20:15
  */

import shapeless._
import shapeless.labelled._
import scala.xml.Node

object CaseClasInheritance {
  trait Super {
    def a: String
  }

  case class Child1(a: String, b: String) extends Super {
    override def toString = s" a = $a, b= $b"

  }

  case class Child2(a: String, c: String) extends Super {
    override def toString = s" a = $a, c= $c"
  }

  trait Extract[L] extends (scala.xml.Node => L)

  implicit object extractHNil extends Extract[HNil] {
    def apply(node: Node): HNil = HNil
  }

  implicit def extractHCons[K <: Symbol, L <: HList]
  (implicit witness: Witness.Aux[K], recur: Extract[L]) =
    new Extract[FieldType[K, String] :: L] {
      def apply(node: Node): ::[FieldType[K, String], L] = {
        val name = witness.value.toString
        val value = (node \ name).text
        field[K](value) :: recur(node)
      }
    }

  implicit def extractCase[C, L]
  (implicit lgen: LabelledGeneric.Aux[C, L], extract: Extract[L]) =
    new Extract[C] {
      def apply(node: Node): C = lgen.from(extract(node))
    }


  abstract class XmlReader[C](extract: Extract[C]) {
    def apply(node: scala.xml.Node) = extract(node)
  }

//  object Child1 extends XmlReader[Child1](implicitly)
//
//  object Child2 extends XmlReader[Child2](implicitly)
}
