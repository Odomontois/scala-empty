val stream = Seq(
  "Tom" -> "#apple @Chris  asdsd #banana @Bob"
)
val hashTags = stream.map { case (user, text) =>
  (user,
    text.split(' ').filter(_.startsWith("#")) match {
      case Array(x,y) => (x,y)
    },
    text.split(' ').filter(_.startsWith("@")) match {
      case Array(x,y) => (x,y)
    })
}
val byTag = hashTags.flatMap {
  case (user, (tag1, tag2), mentions) => Seq(tag1, tag2).map((_, user, mentions))
}

