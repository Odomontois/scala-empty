///**
// * Author: Oleg Nizhnik
// * Date  : 06.11.2015
// * Time  : 16:46
// */
//
//import scala.reflect.ClassTag$;
//
//public class ServiceRegMain {
//    public static void main(String[] args) {
//    final Foo foo = new Foo();
//    final Bar bar = new Bar();
//    Service.pendingServices$.MODULE$.update("baz", foo, ClassTag$.MODULE$.apply(Foo.class));
//    Service.pendingServices$.MODULE$.update("baz", bar, ClassTag$.MODULE$.apply(Bar.class));
//
//    System.out.println(Service.pendingServices$.MODULE$.<Foo>apply("baz", ClassTag$.MODULE$.apply(Foo.class)));
//    System.out.println(Service.pendingServices$.MODULE$.<Bar>apply("baz", ClassTag$.MODULE$.apply(Bar.class)));
//    }
//}
