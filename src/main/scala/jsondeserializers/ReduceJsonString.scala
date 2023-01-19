package spark.jsondeserializers

import testsession.SparkWrapper

object ReduceJsonString extends App with SparkWrapper{

  import org.apache.spark.sql.types._
  import org.apache.spark.sql.functions._
  import sparkSession.implicits._

  val df=Seq(("01","ABC","""{"key1":"valueA","key2":"valueC"}""")).toDF("a","b","c")
  df.show(false)

  val Schema = MapType(StringType, StringType)

  val finalDf = df.withColumn("d", from_json(col("c"), Schema)).
                                      selectExpr("a","b","explode(d)")

  finalDf.show(false)
}
