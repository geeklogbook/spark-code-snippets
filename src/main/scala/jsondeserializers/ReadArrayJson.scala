package spark.jsondeserializers

import testsession.SparkWrapper

object ReadArrayJson extends App with SparkWrapper {

  import org.apache.spark.sql.functions._
  import sparkSession.implicits._

  val sampleJsonStr = """{"items":[ {"Name":"test", "Id":"1"}, {"Name":"sample", "Id":"2"}]}"""

  val json_df = sparkSession
    .read
    .option("multiline", "true")
    .json(Seq(sampleJsonStr).toDS())

  val explodedDF = json_df
    .select("items")
    .withColumn("exploded_items", explode(col("items")))
    .select(col("exploded_items.Id"), col("exploded_items.Name"))

  explodedDF.show()
}
