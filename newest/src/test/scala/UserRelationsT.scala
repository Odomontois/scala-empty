import cats.data.Ior

object UserRelationsT {
  import shapeless.tag
  import tag.@@
  import shapeless.Unwrapped

  trait IsFollowerTag
  type IsFollower = Boolean @@ IsFollowerTag
  def isFollower(b: Boolean): IsFollower = tag[IsFollowerTag][Boolean](b)

  trait IsFollowingTag
  type IsFollowing = Boolean @@ IsFollowingTag
  def isFollowing(b: Boolean): IsFollowing = tag[IsFollowingTag][Boolean](b)

  type Rel = Ior[IsFollowing, IsFollower]

  implicit class OptionTagOps[A](opt: Option[A]) {
    def getOrElseT[T](default: T)(implicit unwrapped: Unwrapped.Aux[A, T]): A = {
      unwrapped.wrap(opt.map(unwrapped.unwrap).getOrElse(default))
    }
  }

  val example: IsFollower = Ior.left(isFollower(true)).left.getOrElseT(false)
}