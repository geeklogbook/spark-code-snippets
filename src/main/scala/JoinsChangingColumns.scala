package spark

import org.apache.spark.sql.DataFrame
import testsession.SparkWrapper

case object JoinsChangingColumns extends App with SparkWrapper {
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  val prodRawDF = Seq(
    ("20200101", "MLU1234",15, "MLU", 42),
    ("20200101", "MLU1234",24, "MLU", 45),
    ("20200101", "MLU1234",58, "MLU", 45),
    ("20200101", "MLU1234",10, "MLU", 45)
  ).toDF("datetrack", "id", "sold_quantity_delta", "site", "con_ventas")

  val devRawDF = Seq(
    ("20200101", "1234",15, "casa", 42),
    ("20200101", "1234",24, "casa", 45),
    ("20200101", "234",58, "casa", 45),
    ("20200101", "234",10, "casa", 45)
  ).toDF("date", "id", "sold_quantity_delta", "site", "con_ventas")

  def monthlyDataFrame(rawDataFrame: DataFrame, subscription:String): DataFrame ={
    val smallDF = rawDataFrame.select("datetrack",
      "id",
      "sold_quantity_delta").
      withColumn("site", substring(col("id"), 1, 3)).
      withColumn("items_con_ventas", when(col("sold_quantity_delta") =!= 0, 1).otherwise(0))

    val qaDataFrame = smallDF.groupBy("date", "site").agg(
      count("id".distinct).as("items-" + subscription),
      sum("items_con_ventas").as("con_ventas-"+ subscription),
      sum("sold_quantity_delta").as("sold_quantity_delta-"+ subscription))

    qaDataFrame
  }

  val prodDf = monthlyDataFrame(prodRawDF, "prod")
  val devDF  = monthlyDataFrame(devRawDF, "dev")

  val qaDataFrame = prodDf.join(devDF, Seq("date", "site"))

  val qaDataFrameOrdered = qaDataFrame.select("date", "site",
                                                    "items-prod", "items-dev",
                                                    "sold_quantity_delta-prod", "sold_quantity_delta-dev",
                                                    "items_con_ventas-prod", "items_con_ventas-dev")

  val test = 0
}
