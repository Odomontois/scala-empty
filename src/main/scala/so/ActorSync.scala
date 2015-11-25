package so

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

import CountingApp._

/**
  * User: Oleg
  * Date: 22-Nov-15
  * Time: 02:42
  */
class CounterActor extends Actor {
  @volatile private[this] var counter = 0

  implicit val to = Timeout(5 seconds)

  def receive: Receive = {
    case Count(id) ⇒ self ! Increment(id, sender)
    case Increment(id, snd) ⇒ snd ! {
      counter += 1
      println(s"req_id=$id, counter=$counter")
      counter
    }
  }
}

class SenderActor extends Actor {
  val counter = system.actorOf(Props[CounterActor])

  def receive: Actor.Receive = {
    case n: Int => println(s" -> $n")
    case StartCounting =>
      counter ! Count(1)
      counter ! Count(2)
      counter ! Count(3)
      counter ! Count(4)
      counter ! Count(5)
  }
}

sealed trait ActorMessage
case class Count(id: Int = 0) extends ActorMessage
case class Increment(id: Int, requester: ActorRef) extends ActorMessage
case object StartCounting

object CountingApp {
  implicit val to = Timeout(5 seconds)

  def main(args: Array[String]) {

    val sender = system.actorOf(Props[SenderActor])

    sender ! StartCounting
  }

  val system = ActorSystem("count")


  // Bye!


  def handleResponse(future: Future[Any]): Unit = {
    future.onComplete {
      case Success(n: Int) => println(s" -> $n")

      case Failure(t) => println(t.getMessage)
    }
  }
}