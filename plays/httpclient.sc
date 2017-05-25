import java.lang.reflect.{Method, Proxy}

class Client

abstract class DynamicClient extends Client {
  def swap: Client
}

def mkClient =
  Proxy.newProxyInstance(
    classOf[Client].getClassLoader,
    Array(classOf[Client]),
    new DynamicClientHandler
  ).asInstanceOf[DynamicClient]


class DynamicClientHandler extends java.lang.reflect.InvocationHandler {
  val client = new Client{}

  def invoke(proxy: AnyRef, method: Method, args: Array[AnyRef]): AnyRef =
    if (method.getDeclaringClass == classOf[DynamicClient])
      swap
    else method.invoke(client, args: _*)


  def swap = createNewClient

  def createNewClient = mkClient
}

mkClient.swap