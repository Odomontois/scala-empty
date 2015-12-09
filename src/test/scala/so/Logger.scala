/**
  * Author: Oleg Nizhnik
  * Date  : 25.11.2015
  * Time  : 11:42
  */
package so
import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
class LoggerImpl(val c: Context) {
  import c.universe._

  def getClassSymbol(s: Symbol): Symbol = if (s.isClass) s else getClassSymbol(s.owner)

  def logImpl(msg: Expr[String]): Expr[Unit] = {
    val cl = getClassSymbol(c.internal.enclosingOwner).toString
    val time = c.Expr[String](q"new java.util.Date().toString")
    val logline = c.Expr[String](q""" "[" + $cl + " : " + $time + "]" + $msg """)
    c.Expr[Unit](q"println($logline)")
  }
}

object Logger {
//  def warning(msg: String): Unit = macro LoggingImpl.logImpl
}
