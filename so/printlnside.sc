def foo = {
  println("Good to see you sir!")
  1
}

def bar = {
  println("I hate you")
  2
}

def baz = {
  println("Just joking")
  3
}

def sumA = {
  val x = foo
  val y = bar
  val z = baz
  x + y + z
}

def sumB = {
  val x = foo
  val z = baz
  val y = bar
  x + y + z
}


println(s"By the way your result is $sumA")

println(s"By the way your result is $sumB")


