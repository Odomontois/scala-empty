sealed trait Parent

case object Boy extends Parent

case object Girl extends Parent

trait HasGirl {
   val x: Girl.type
  }

case class Thing(x: Boy.type) extends HasGirl

Thing(Boy).x

val b : HasGirl = Thing(Boy)


b.x