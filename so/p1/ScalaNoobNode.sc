case class Node(visited: Boolean = false,  mDistance: Int  = 10000, prevLoc: String = " ")

case class Route(startLocation: String, endLocation: String, distance: Int)


object test{
  def main(args: Array[String]){

    val l = List(
      Route( "Kruthika's abode", "Mark's crib",  10),
      Route( "Mark's Crib", "Kirk's Farm",  9))

    val b = for((i, index) <- l.zipWithIndex) yield Node(prevLoc = i.startLocation)

    println(b)
  }
}
test.main(Array())




