val text: Map[String, String]  = ???


val keyword: String = ""
def textContains(str: String): String => Boolean = str match {
  case "" => Function.const(true)
  case _ => text(_).contains(str)
}
val containsKeyword = textContains(keyword)
for {
  /* ...*/
  a<- None if  Seq("asdasd", "asdasd").exists(textContains(keyword))
} yield a
