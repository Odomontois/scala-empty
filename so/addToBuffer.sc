import scala.collection.mutable.Buffer
var sum = Buffer[Double](1.0,2.0)
sum.append(3.0)
val add = Seq[Double](4.0,5.0,6.0)
for( i <- 0 until sum.size ) sum.update(i,sum(i)+add(i))

add.view.zipAll()