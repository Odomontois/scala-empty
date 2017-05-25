import doobie.imports._

import scalaz.effect.IO
import doobie.syntax.string.Builder
import shapeless._
import scalaz.syntax.show._
import scalaz._
import scalaz.std.list._
import scalaz.std.string._
import shapeless.record._
import shapeless.syntax.singleton._
import scalaz.concurrent.Task


val xa = DriverManagerTransactor[Task](
  "org.mariadb.jdbc.Driver", "jdbc:mariadb://localhost:3306/doobieplay", "doobie", "pass123"
)

case class Country(code: String, name: String, population: Long, gnp: Option[Double])

def biggerThan(minPop: Int) = (sql"""
  select code, name, population, gnp
  from country
  where population > $minPop
  order by population asc
""" : Builder[Int :: HNil]).query[Country]

val countriesName = biggerThan(75000000)
  .list
  .transact(xa)
  .unsafePerformSync
  .map(_.name)
  .shows

def populationIn(range: Range, codes: NonEmptyList[String]) =
  {
    implicit val codesParam = Param.many(codes)

    sql"""
select code, name, population, gnp
from country
where population > ${range.min} and population < ${range.max}
and   code in (${codes: codes.type})
order by population asc
""" : Builder[Int :: Int :: codes.type :: HNil]
  }.query[Country]

populationIn(25000000 to 75000000, NonEmptyList("ESP", "USA", "FRA"))
  .list
  .transact(xa)
  .unsafePerformSync
  .map(_.name)
  .shows


java.util.function.BiConsumer