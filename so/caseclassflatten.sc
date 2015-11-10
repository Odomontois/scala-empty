import monocle.macros.Lenses

@Lenses
case class Info(name: String,
                age: Int,
                depts: List[String]
               )
@Lenses
case class User(id: String,
               info: Info
              )

import monocle.function.Index._
import monocle.std.list._
object User {
  val name = info ^|-> Info.name
  val age = info ^|-> Info.age
  val mainDept = info ^|-> Info.depts ^|-? index(0)
}
val oleg = User("odomontois", Info("Oleg", 23, List("python", "ABAP")))


User.mainDept.getOption(oleg) == Some("python")
(User.age.set(28) andThen User.mainDept.set("scala")) (oleg) ==
  User("odomontois",Info("Oleg",28,List("scala", "ABAP")))

