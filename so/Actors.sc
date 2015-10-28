import akka.actor.{Stash, ActorRef, Actor}
object All {
  class Node extends Actor {
    import Node._
    val state1: Int = 4

    def receive = {
      case getState =>
        sender ! State(state1)

    }
  }

class Master extends Actor with Stash {
  import Master._
  def receive: Receive = {
    case Action(node) =>
      val intermediate = scala.util.Random.nextLong()
      node ! Node.GetState
      context.become(waitingForState(calc = intermediate), discardOld = false)
  }

  def waitingForState(calc: Long): Receive = {
    case Node.State(state) ?
      // do something with state
      context.unbecome()
      unstashAll()
    case _ ? stash()
  }
}

  object Master {
    case class Action(node: ActorRef)
  }

  object Node {
    case object GetState
    case class State(state: Int)
  }
}

