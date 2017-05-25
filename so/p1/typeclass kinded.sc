trait AttributeParser[T] {
  def parse(attribute: String): T
}

implicit def optionAttributeParser[T](implicit parser: AttributeParser[T]) =
  new AttributeParser[Option[T]] {
    def parse(attribute: String): Option[T] =
      if (attribute.isEmpty) None else Some(parser.parse(attribute))
  }

trait AttributeParserK[F[_]] {
  def parseK[T](attribute: String)(implicit itemParser: AttributeParser[T]): F[T]
}

object AttributeParser{
  implicit def kindParser[F[_], T](implicit itemParser: AttributeParser[T], parserK: AttributeParserK[F]) =
    new AttributeParser[F[T]] {
      def parse(attribute: String): F[T] = parserK.parseK(attribute)
    }
}