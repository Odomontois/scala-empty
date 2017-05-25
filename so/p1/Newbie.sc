val isVowel = Set('a', 'e', 'i', 'o', 'u')

//def stripVowelsRecursive(str: String): String = {
//  if(str.length() == 0) " "
//  else
//    for(ch <- str) yield {
//      if(ch.isDigit) ch
//      else ' ' + stripVowelsRecursive(str.tail)
//    }
//}

def stripVowelsRecursive(str: String): String =
  if (str.isEmpty) ""
  else (if (isVowel(str.head)) str.head else "") + stripVowelsRecursive(str.tail)

def stripVowelsRecursiveOpt(str: String): String = {
  def go(acc: List[Char], chars: Seq[Char]): String = chars match {
    case ch +: rest if isVowel(ch) => go(acc, rest)
    case ch +: rest => go(ch :: acc, rest)
    case Nil => acc.reverse.mkString
  }
  go(Nil, str)
}

def stripVowelsLoop(str: String): String = for (ch <- str if isVowel(ch)) yield ch

def stripVowelsSimples(str: String): String = str.filter(isVowel)
