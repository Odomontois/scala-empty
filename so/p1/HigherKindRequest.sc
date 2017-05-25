import scala.language.higherKinds

type Const[T] = T

sealed abstract class Request[F[_], A]

case class GetOne[X](id: Int) extends Request[Const, X]
case class GetMany[X]() extends Request[Seq, X]

trait Client {
  def getOne[X]: X
  def getMany[X]: Seq[X]
}

trait HandleRequest[R <: Request[F, A], F[_], A] {
  def apply(request: R, client: Client): F[A]
}

implicit def handleGetOne[X] = new HandleRequest[GetOne[X], Const, X] {
  def apply(request: GetOne[X], client: Client): X = client.getOne
}

implicit def handleGetMany[X] = new HandleRequest[GetMany[X], Seq, X] {
  def apply(request: GetMany[X], client: Client): Seq[X] = client.getMany
}


implicit class ClientOps(val client: Client) {
  def get[R <: Request[F, A], F[_], A](request: R)(implicit handle: HandleRequest[R, F, A]): F[A] =
    handle(request, client)
}

