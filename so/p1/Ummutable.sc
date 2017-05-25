import scala.collection.mutable.ListBuffer

val buf = new ListBuffer[Int]()
buf ++= Seq(1,2,3)
val u =     buf.toIterable
val lst: List[Int] = buf.toIterable.toList
System.identityHashCode(lst)

println(lst) //List(1,2,3)
System.identityHashCode(lst)
buf ++= Seq(4,5,6)
System.identityHashCode(lst)
println(lst) //List(1,2,3,4,5,6)
System.identityHashCode(lst)