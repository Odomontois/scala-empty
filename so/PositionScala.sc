class Position(lat: java.lang.Double, lon: java.lang.Double)
case class PositionScala(latitude: Double, longitude: Double)
object PositionScala {
  implicit def toJava(p: PositionScala): Position = new Position(p.latitude, p.longitude) // Position is the Java class
}

object routeScala {
  val shape = List((1, 2), (2, 3)).map { case (x, y) => PositionScala(x, y) }
  val opt: Option[PositionScala] = Some(PositionScala(1,2))
}

val p: Option[Position] = routeScala.opt.map(p => p: Position)