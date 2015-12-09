package so

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

class VerboseImpl(val c: Context) {
  import c.universe._

  def describe[T: c.WeakTypeTag](expr: c.Expr[T]): c.Expr[(String, String, T)] = {
    val repr = expr.tree.toString
    val typeName = weakTypeOf[T].toString
    c.Expr[(String, String, T)](q"($repr, $typeName, $expr)")
  }

}

object Verbose{
  def apply[T](expr: T): (String, String, T) = macro VerboseImpl.describe[T]
}
