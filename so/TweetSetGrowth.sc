import java.nio.file.{Paths, Files}

import scalaz._
import std.anyVal._
import std.option._
import std.tuple._
import syntax.all._
import syntax.std.all._
import WriterT._

val a = 1.node()
a.drawTree
type Counted[A] = Writer[(Long, Long), A]
val createCost = tell((1L, 0L))
val unionCost = tell((0L, 1L))
object Tweets {
  def empty: TweetSet = Empty
  val nonEmpty: (Tweet, TweetSet, TweetSet) => TweetSet = NonEmpty(_, _, _)
  case class Tweet(text: Int)
  implicit val showTweet = Show.shows[Tweet](_.toString)

  sealed trait TweetSet {
    def union(that: TweetSet): Counted[TweetSet]
    def incl(tweet: Tweet): Counted[TweetSet]
    def toTree: Tree[Option[Tweet]]
    def self: TweetSet = this
  }
  case object Empty extends TweetSet {
    def union(that: TweetSet): Counted[TweetSet] = that.point[Counted]
    def incl(that: Tweet): Counted[TweetSet] =
      createCost >> nonEmpty(that, Empty, Empty).point[Counted]

    def toTree = Tree.leaf(none)
  }
  case class NonEmpty(elem: Tweet,
                      left: TweetSet,
                      right: TweetSet) extends TweetSet {
    def incl(x: Tweet): Counted[TweetSet] =
      if (x.text < elem.text) for {
        l <- left.incl(x)
        _ <- createCost
      } yield new NonEmpty(elem, l, right)
      else if (x.text > elem.text) for {
        r <- right.incl(x)
        _ <- createCost
      } yield new NonEmpty(elem, left, r)
      else self.point[Counted]

    def toTree = elem.some.node(left.toTree, right.toTree)
    def union(that: TweetSet): Counted[TweetSet] = for {
      first <- right union left
      second <- first union that
      last <- second incl elem
      _ <- unionCost
    } yield last
  }

  object TweetSet extends (Tweet => TweetSet) {
    def apply(tweet: Tweet) = (Empty incl tweet).run._2
  }
}
import Tweets._
val out = Files.newBufferedWriter(Paths.get("d:/prog/projects/sbt/empty/trees.out"))
List.range(0, 40, +1)
  .map(Tweet andThen TweetSet)
  .scanLeft(empty.point[Counted]) {
    (c, elem) => for {
      prev <- c
      res <- prev union elem
    } yield res
  }
  .map(_.run)
  .zipWithIndex
  .foreach { case (((c, u), s), i) =>
    out.write(f"$i[created:$c, united:$u]:\n")
    out.write(s.toTree.drawTree + "\n")
  }
out.flush()
out.close()