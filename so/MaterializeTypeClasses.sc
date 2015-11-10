import shapeless._
trait Tc[T]

case class Cc(a: String, b: Int)

trait ResolveTC[L] {
  type Out <: HList
  val out: Out
}

implicit object ResolveHNilTC extends ResolveTC[HNil] {
  type Out = HNil
  val out: HNil = HNil
}

implicit def resolveHListTC[X, XS <: HList](implicit tc: Tc[X], rest: ResolveTC[XS]) =
  new ResolveTC[X :: XS] {
    type Out = Tc[X] :: rest.Out
    val out = tc :: rest.out
  }

class TCResolver[C] {
  def get[L <: HList](implicit gen: Generic.Aux[C, L], resolve: ResolveTC[L]): resolve.Out = resolve.out
}

def resolveTypeClasses[C] = new TCResolver[C]

implicit case object StringInst extends Tc[String]
implicit case object IntInst extends Tc[Int]

val g = Generic[Cc]
val resolved = resolveTypeClasses[Cc].get




