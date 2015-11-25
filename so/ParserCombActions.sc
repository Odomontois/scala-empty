import org.parboiled2._


import scala.util.{Success, Failure, Try}

object Actions {
  case class Action(name: String, elems: Seq[String])

  def apply(input: String): Try[Seq[Action]] = new Actions(input).Input.run()
}

class Actions(val input: ParserInput) extends Parser {
  import Actions._

  def append[T]: (Seq[T], T) => Seq[T] = _ :+ _

  def Input = rule {WS ~ "actions{" ~ WS ~ ActionList ~ WS ~ "}" ~ WS ~ EOI}

  def ActionList = rule {FirstAction ~ zeroOrMore(WS ~ ActionDef ~> ((_: Seq[Action]) :+ _))}

  def FirstAction = rule {ActionDef ~> (Vector(_))}

  def ActionDef = rule {(capture(Name) ~ "=[" ~ ElementList ~ "]") ~> Action.apply _}

  def ElementList = rule {
    WS ~ FirstElement ~ zeroOrMore(
      WS ~ "," ~ WS ~ Element ~> ((_: Seq[String]) :+ _))
  }

  def FirstElement = rule {Element ~> (Vector(_))}

  def Element = rule {capture(Name)}

  def Name = rule {oneOrMore(CharPredicate.Alpha | '_')}

  def WS = rule {zeroOrMore(anyOf(" \n\r\t\f"))}
}

val actions =
  Actions("actions{aaa=[bb,cc]}")
//Actions(
//  """
//   actions{
//     ACTION_NAME=[ELEMENT,ANOTHER_ELEMENT,SOMETHING_ELSE]
//     ANOTHER_ACTION=[SOMETHING_HERE,AN0THER_ONE]
//   }""") match {
//  case Success(s) => println(s)
//  case Failure(ex) => ex.printStackTrace()
//}
"2".r.findAllIn("232").end

