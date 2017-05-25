import enumeratum.values.{IntEnum, IntEnumEntry}
import enumeratum._

sealed abstract class Sex(val name: String, val value: Int) extends IntEnumEntry

object Sex extends IntEnum[Sex]{
  def values = findValues
  case object Female extends Sex("FEMALE", 1)
  case object Male extends Sex("MALE", 2)
}

Sex.withValue(2).name

object olol extends Enumeration{
  val aaa = Value
}

Sex.withValueOpt()