import Math._
object Clarence {
  def main(args: Array[String]) {
    val ip = args(0).map("123456789.0".toCharArray.indexOf)
    println((ip.tail, ip).zipped.map((c, p) => sqrt(pow(c % 3 - p % 3, 2) + pow(c / 3 - p / 3, 2))))
  }
}