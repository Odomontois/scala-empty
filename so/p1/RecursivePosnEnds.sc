case class FilePosn(lineNum: Int, tabs: Int, spaces: Int, fileName: String) {
  def posnString(indentSize: Int): String = ???
}

trait PosnEnds {
  def startPosn: FilePosn
  def endPosn: FilePosn
  def multiLine: Boolean = startPosn.lineNum != endPosn.lineNum
  def OneLine: Boolean = startPosn.lineNum == endPosn.lineNum
  def indent: Int = startPosn.tabs
  def startLine: Int = startPosn.lineNum
  def endLine: Int = endPosn.lineNum
}

trait MakePosnEnds[X] extends (X => PosnEnds)

object PosnEnds {
implicit object idMakePosnEnds extends MakePosnEnds[PosnEnds] {
  def apply(x: PosnEnds) = x
}

implicit def seqMakePosnEnds[X](implicit recur: MakePosnEnds[X]) = new MakePosnEnds[Seq[X]] {
  def apply(x: Seq[X]): PosnEnds = new PosnEnds {
    val thisSeq = x.map(recur)
    override def startPosn: FilePosn = thisSeq.headOption.fold(void)(_.startPosn)
    override def endPosn: FilePosn = thisSeq.lastOption.fold(void)(_.endPosn)
  }
}

val void = new FilePosn(0, 0, 0, "There is no File position") {
  override def posnString(indentSize: Int): String = "No File Posn: "
}

def empty = new PosnEnds {
  def startPosn: FilePosn = void
  def endPosn: FilePosn = void
}

  implicit def toPosnEnds[X](x: X)(implicit make: MakePosnEnds[X]): PosnEnds = make(x)
}

import PosnEnds._

val u = Seq(Seq(Seq(empty))).startLine

implicitly[MakePosnEnds[Seq[Seq[Seq[PosnEnds]]]]]