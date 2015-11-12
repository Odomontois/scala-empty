import scalaz.Tree._
import scalaz._

import scalaz.syntax.tree._


val dirtree = "E:".node(
  "Work".node(
    "{PROJECT}".node(
      "build.sbt".leaf,
      "src".node("main".node("scala".node(
        "Hello.scala".leaf)))
    )
  ))

val dirtree2 = "E:".node(
  "Work".node(
    "{PROJECT}".node(
        "Hello.scala".leaf)
  ))


implicit object showstr extends Show[String]{
  override def shows(f: String): String = f
}

println(dirtree.drawTree)
println(dirtree2.drawTree)