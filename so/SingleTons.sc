
object xxx {
  object Pattern {
    def compile(s: String): String = s
  }

  lazy val foo: () => String = {
    val pattern = Pattern.compile("pattern")

    def result(): String = pattern

    println("hello")

    result _
  }
}
