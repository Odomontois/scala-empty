import java.nio.file.{Paths, Files}
import java.sql.Date
import play.api.libs.json._
import scala.collection.JavaConverters._
import scala.io.Source
val dir = "d:\\docs\\awt\\results\\"

case class PriceDate(price: Double, depart_date: Date)

implicit val priceDateReads = Json.reads[PriceDate]

def conv(file: String) = {
  val src = Source.fromFile(dir + file + ".json").getLines().mkString
  val res = (Json.parse(src) \ "result").as[Seq[PriceDate]]

  Files.write(Paths.get(dir + file + ".out"),
      Seq(res.mkString("List(\n",",\n", "\n)")).asJava)
}

conv("res1")
conv("res2")







