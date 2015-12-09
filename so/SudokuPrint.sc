object mod {
  object Sudoku {
    implicit class GroupStr[X](xs: Seq[X]) {
      def groupStr(sep: String): String =
        xs.grouped(3).map(
          _.mkString(sep, sep, sep)
        ).mkString
    }
  }

  case class Sudoku(grid: List[List[Int]]) {
    import Sudoku._
    def lineSep = ("-" * 9).toList.groupStr("+") + "\n"
    override def toString = grid.map(_.groupStr("|") + "\n").groupStr(lineSep)
  }

  println(Sudoku(List.range(0, 9).map(
    i => List.range(0, 9).map( j => (i + j) % 9 + 1))))
}

val a = List(("name1","add1","city1",10),("name1","add1","city1",10),

  ("name2","add2","city2",10),("name2","add2","city2",20),("name3","add3","city3",20))

a.map{case x@(name, add, city, _) => (name,add,city) -> x}.toMap.values.toList