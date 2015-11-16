import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.types.{StringType, StructField, StructType}

class UserTransformConfig {
  def getConfigString(name: String): Option[String] = ???
}

class PhaseLogger
object byteUtils {
  def bytesToUTF8String(r: Array[Byte]): String = ???
}

class HashingTF {
  def transform(strs: Array[String]): Array[Double] = ???
}

object genderModel {
  def predict(v: Array[Double]): Double = ???
}

def transform(sqlContext: SQLContext, rdd: RDD[Array[Byte]], config: UserTransformConfig, logger: PhaseLogger): DataFrame = {
  val idColumnName = config.getConfigString("column_name").getOrElse("id")
  val bodyColumnName = config.getConfigString("column_name").getOrElse("body")
  val genderColumnName = config.getConfigString("column_name").getOrElse("gender")

  // convert each input element to a JsonValue
  val jsonRDD = rdd.map(r => byteUtils.bytesToUTF8String(r))

  val hashtagsRDD: RDD[(String, String, String)] = jsonRDD.mapPartitions(r => {
    // register jackson mapper (this needs to be instantiated per partition
    // since it is not serializable)
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)

    r.map { tweet =>
      val rootNode = mapper.readTree(tweet)
      val tweetId = rootNode.path("id").asText.split(":")(2)
      val tweetBody = rootNode.path("body").asText
      val tweetVector = new HashingTF().transform(tweetBody.split(" "))
      val result = genderModel.predict(tweetVector)
      val gender = if (result == 1.0) {"Male"} else {"Female"}

      (tweetId, tweetBody, gender)

    }
  })

  val rowRDD: RDD[Row] = hashtagsRDD.map(x => Row(x._1, x._2, x._3))
  val schema = StructType(Array(StructField(idColumnName, StringType, true), StructField(bodyColumnName, StringType, true), StructField(genderColumnName, StringType, true)))
  sqlContext.createDataFrame(rowRDD, schema)
}
