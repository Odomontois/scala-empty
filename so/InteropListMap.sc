import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import java.util
class Foo extends IFoo{
  override def getMaps: util.List[util.Map[String,AnyRef]]  = {
    List(Map("A" -> "B").mapValues(x => x: AnyRef).asJava)
  }
}
//class Foo2 extends IFoo{
//  override def getMaps: util.List[util.Map[String,AnyRef]]  = {
//    List(Map("A" -> "B")).aresJava
//  }
//}
