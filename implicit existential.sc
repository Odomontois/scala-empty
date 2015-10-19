import shapeless._
import poly._
import ops.hlist.Mapper

trait View[Record] {
  type Result <: HList
  def apply(r: Record): Result
}

object View extends LowPriorityLiftFunction1 {
  type Aux[Record, L <: HList] = View[Record] {type Result = L}
  implicit def atView[Record](implicit view: View[Record]) = at[Record](view.apply(_))
}

object toHView extends ->((_: Int) + 1)

implicit def provideView[Record, L <: HList, Out <: HList]
(implicit generic: Generic.Aux[Record, L],
 mapper: Mapper.Aux[toHView.type, L, Out])
: View.Aux[Record, Out] =
  new View[Record] {
    type Result = Out

    def apply(r: Record) = mapper(generic.to(r))
  }

case class Viewable(x: Int, y: Int, z: Int)
case class NotViewable(x: Int, y: Long, z: Int)

val view = View(Viewable(1, 2, 3))

val noView = View(NotViewable(1, 2, 3))

view.head