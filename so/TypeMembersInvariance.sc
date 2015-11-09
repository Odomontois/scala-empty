/**http://stackoverflow.com/questions/33458782/scala-type-members-variance */

trait Table[+A] {
  type RowType <: Seq[A]
}

def mkTable[A]: Table[A]  = new Table[A] {
  type RowType = Seq[A]
}


val tupleTable = mkTable[(String, String)]
val prodTable: Table[Product] = tupleTable
