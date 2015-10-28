import scala.language.{existentials, higherKinds}
object experiment {
  class MultiSignalElement[+T]

  abstract class MultiSignal[+T] {
    type Element[+X] <: MultiSignalElement[X]

    val element : Element[T]
    def transform[E[+X] >: Element[X], R[+X] <: MultiSignal[X]](builder : MultiSignalBuilder[E, R]) : R[T] =
      builder.buildNew(element)
  }
  abstract class MultiSignalBuilder[-E[+X] <: MultiSignalElement[X], +R[+X] <: MultiSignal[X]] {
    def buildNew[T](element : E[T]) : R[T]
  }
  object myBuilder extends MultiSignalBuilder[MultiSignalElement, MultiSignal] {
    def buildNew[T](element : MultiSignalElement[T]) = new MultiSignal[T](){
      type Element[X] = MultiSignalElement[X]
    }
  }

  val multiSignal = new MultiSignal[Int] {
    type Element[+X] = MultiSignalElement[X]
  }

  multiSignal.transform(myBuilder) //compiles
  multiSignal.transform[MultiSignalElement, MultiSignal](myBuilder) //compiles
}