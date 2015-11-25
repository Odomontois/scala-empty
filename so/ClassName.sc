import java.util.Date
import so.Logger

import scala.util.DynamicVariable
object LOGGER {
  val caller = new DynamicVariable[String]("---")
  def time = new Date().toString
  def warning(msg: String) = println(s"[${caller.value} : $time] $msg")
}

trait Logging {
  def logged[T](action: => T) = LOGGER.caller.withValue(this.getClass.getName)(action)
}

class testing{
  def test = {
    //some actions
    Logger.warning("something happen")
    //some other actions
  }
}

val t = new testing

t.test