import shapeless.Lazy

case class Custom(value: Int)
case class Custom2(value: Float)

case class MappedEncoding[I, O](f: I => O)

trait Decoders {
  type BaseDecoder[T] = () => T
  type Decoder[T] <: BaseDecoder[T]
}

trait ConcreteDecoders extends Decoders {
  type Decoder[T] = ConcreteDecoder[T]

  case class ConcreteDecoder[T](decoder: () => T) extends BaseDecoder[T] {
    def apply(): T = decoder()
  }

  implicit def optionDecoder[T](implicit d: Decoder[T]): Decoder[Option[T]] =
    ConcreteDecoder[Option[T]](() => Some(d()))


  implicit def intDecoder: Decoder[Int] = ConcreteDecoder[Int](() => 1)
  implicit def floatDecoder: Decoder[Float] = ConcreteDecoder(() => 1)

  implicit def mappedDecoder[I, O](implicit mapped: MappedEncoding[I, O], decoder: Lazy[Decoder[I]]): Decoder[O] =
    ConcreteDecoder[O](() => mapped.f(decoder.value()))
}

class ConcreteContext extends ConcreteDecoders

case class TestObject() {

  implicit val customDecoder = MappedEncoding[Int, Custom](Custom)
  implicit val custom2Encoder = MappedEncoding[Custom2, Float](_.value)
  // 1
  implicit val custom2Decoder = MappedEncoding[Float, Custom2](Custom2)

  def a(c: ConcreteContext): Unit = {
    import c._
    implicitly[Decoder[Option[Custom]]] // 2
    implicitly[Decoder[Float]] // 3
    implicitly[Decoder[Option[Float]]]
    ()
  }
}


object Main extends App {
  implicit val c = new ConcreteContext()

  TestObject().a(c)
  // TestObject(a).()
}