case class Pack(name: String, age: Int, dob: String)

case class NewPack(name: String, pack: List[Pack])

object NewPack{
  def getNewPackList(data: List[Pack]): List[NewPack] =
    data.map{case pack@Pack(name, _, _) => NewPack(name, List(pack))}

def newPacksGrouped(data: List[Pack]): List[NewPack] =
  data.groupBy(_.name).map((NewPack.apply _).tupled).toList
}
