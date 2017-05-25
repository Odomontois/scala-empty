import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.{Keep, Sink, Source}

def assemble[A, B, C](source: Source[A, NotUsed])
                     (f: A => Source[B, NotUsed], g: A => Sink[B, C]): Source[C, NotUsed] = {

  source.flatMapConcat(f).
//    .map(f)
//    .map(_.toMat(sink)(Keep.right))
//    .map(_.run())
}