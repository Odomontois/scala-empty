import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import scalaz._
import scalaz.std.scalaFuture._
import scalaz.syntax.monad._
import scalaz.OptionT._
import scalaz.syntax.std.option._


import scala.concurrent.{Await, Future}

object MatchData {
  def numberOfCurrentMatches(email: String) = Future(1000)
}


def incrementNumberOfMatches(email: String): Future[Unit] = Future(())


def genDemoMatches(email: String, it: Int): Future[Unit] = Future(())
def genIntMatches(email: String, it: Int): Future[Unit] = Future(())
def genSchoolWorkMatches(email: String, it: Int): Future[Unit] = Future(())
def generateUsers(email: String): Future[Unit] = Future(())

def generateAll(email: String, iterationNumber: Int): Future[Unit] = for {
  _ <- generateUsers(email)
  _ <- genDemoMatches(email, iterationNumber)
  _ <- genIntMatches(email, iterationNumber)
  _ <- genSchoolWorkMatches(email, iterationNumber)
} yield ()

def generateStep(email: String, limit: Int)(iterationNumber: Int): OptionT[Future, (Int, Int)] = {
  val matches = MatchData.numberOfCurrentMatches(email) map (_.some)
  for {
    matches <- OptionT.apply(matches) if matches < limit
    _ <- generateAll(email, iterationNumber).liftM
    _ <- incrementNumberOfMatches(email).liftM
    next = iterationNumber + 1
  } yield (matches, next)
}


def matchGeneration(email: String, itNum: Int): Future[Stream[Int]] =
  StreamT.unfoldM(0)(generateStep(email, 150) _ andThen (_.run)).toStream.map(_.force)



