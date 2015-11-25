import scala.concurrent.Future

def zipStrings(first: String,
               second: String,
               comb: (Char, Char) => String = (f, _) => f.toString,
               placeholder: Char = '_') =
  first.zipAll(second, '_', '_').map {
  case (c, `placeholder`) => c
  case (`placeholder`, c) => c
  case (f, s) => comb(f, s)
}.mkString

zipStrings("L_", "_E")

zipStrings("--L-", "-E--", placeholder = '-')

Vector(1)
