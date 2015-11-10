object xxx {
  object Pattern {
    def compile(s: String): String = s
  }

  lazy val foo: () => String = {
    val pattern = Pattern.compile("pattern")

    () ⇒ pattern
  }

  def main(args: Array[String]) {
    println("asdasd")

    println(xxx.foo)

    println(xxx.foo())
  }
}

