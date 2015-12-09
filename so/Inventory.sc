case class Item(val brand: String, val count: Int)

case class Inventory(items: Map[String, Int] = Map.empty.withDefaultValue(0)) {
  def modified(brand: String, op: Int => Int): Inventory = {
    val item = brand -> op(items(brand))
    copy(items + item)
  }

  override def productIterator: Iterator[Any] = items.iterator

  def add(amount: Int, item: Item): Inventory = modified(item.brand, _ + amount)

  def subtract(amount: Int, item: Item): Inventory = modified(item.brand, _ - amount)

  def apply(brand: String): Item = Item(brand, items(brand))
}

