import shapeless._
import shapeless.ops.hlist.LeftFolder
import shapeless.ops.record.Updater
import syntax.singleton._
import record._
val rec = 'x ->> "One" :: 'y ->> 1 :: HNil
val upd = 'z ->> true :: 'x ->> "Yes" :: HNil
object updateAll extends Poly2 {
  implicit def updateOne[L <: HList, F](implicit update: Updater[L, F]) = at[L, F]((l, f) => update(l, f))
}

implicit class UpdateAllOps[L <: HList](record: L) {
  def ++>[U <: HList](updates: U)(implicit fl: LeftFolder[U, L, updateAll.type]): fl.Out =
    fl(updates, record)
}

val a = rec ++> upd
val b = 'x ->> "Yes" :: 'y ->> 1 :: 'z ->> true :: HNil
rec + ('u ->> 4)

val str = "Yes".asInstanceOf[String with Serializable]

rec ++> ('x ->> str :: HNil)
