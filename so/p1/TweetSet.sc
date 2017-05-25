import scala.util.Random
case class Tweet(text: String)
abstract class TweetSet {
  def union(that: TweetSet): TweetSet
  def rankInsert(tweet: Tweet, rank: Int): TweetSet
  def incl(tweet: Tweet): TweetSet = rankInsert(tweet, Random.nextInt())
  def toList: List[Tweet]
  def depth: Int
}
case object Empty extends TweetSet {
  def depth = 0
  def union(that: TweetSet): TweetSet = that
  def rankInsert(tweet: Tweet, rank: Int) = new NonEmpty(tweet, Empty, Empty, rank)
  def toList = List()
}

case class NonEmpty(elem: Tweet,
                    left: TweetSet,
                    right: TweetSet,
                    rank: Int) extends TweetSet {
  lazy val depth = (left.depth max right.depth) + 1
  def attachToParent(tweet: Tweet, rank: Int) = if (tweet.text < elem.text)
    NonEmpty(tweet, Empty, this, rank)
  else NonEmpty(tweet, this, Empty, rank)

  def rankInsert(tweet: Tweet, rank: Int) =
    if (tweet.text == elem.text) this
    else if (rank > this.rank) attachToParent(tweet, rank)
    else if (tweet.text < elem.text) copy(left = left.rankInsert(tweet, rank))
    else copy(right = right.rankInsert(tweet, rank))

  def toList = left.toList ++ (elem :: right.toList)
  def union(that: TweetSet): TweetSet = if (depth > that.depth) that.union(this)
  else (that /: toList)(_ incl _ )
}

object TweetSet {
  def empty: TweetSet = Empty
  def apply(tweets: Tweet*) = (empty /:  tweets)( _ incl _ )
}

(1 to 1000000).view map (_.toString) map Tweet map (TweetSet(_)) reduce (_ union _) depth

