import monocle.Optional
object package1 {
  import monocle.Iso
  import monocle.macros.Lenses
  @Lenses
  case class Name(first: String, last: String, mid: Option[String]) {
    def fullName = s"$first ${mid.fold("")(_ + " ")}$last"
  }

  object Name {
    val midO = Optional.apply(mid.get)(Some.apply[String]  _ andThen mid.set)

    val fullName = Iso[Name, String](_.fullName)(_.split(' ') match {
      case Array() => Name("", "", None)
      case Array(name) => Name(name, "", None)
      case Array(first, last) => Name(first, last, None)
      case Array(first, mid, last, _*) => Name(first, last, Some(mid))
    })

  }
}

object package2 {

  import monocle.macros.Lenses
  import package1._

  @Lenses
  case class User(name: Name, age: Int)

  object User {
    val fullName = name ^<-> Name.fullName
  }
}

package1.Name.mid

