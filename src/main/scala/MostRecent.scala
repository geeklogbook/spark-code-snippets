package spark

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, concat, expr, lit, max}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window
import testsession.SparkWrapper


object MostRecent extends App with SparkWrapper {
  import sparkSession.implicits._

  println("Start")

  val visits = Seq(
    ("ITEM4449", 33, "https://somefolder@some.net/20200514-1/somename.json"),
    ("ITEM4450", 16, "https://somefolder@some.net/20200514-1/somename.json"),
    ("ITEM1111", 88, "https://somefolder@some.net/20200514-2/somename.json"),
    ("ITEM4453", 64, "https://somefolder@some.net/20200514-1/somename.json"),
    ("ITEM1111", 12, "https://somefolder@some.net/20200514-1/somename.json")).
    toDF("itemId", "visits", "path")

  println("Original")
  visits.show(false)

  println("discardByTrackingCodeJoin")
  val discardByTrackingCodeJoinDataFrame = FilterFunctions.discardByTrackingCodeJoin(visits)
  discardByTrackingCodeJoinDataFrame.show(false)

  println("discardByTrackingCodeFilter")
  val discardByTrackingCodeFilterDataFrame = FilterFunctions.discardByTrackingCodeFilter(visits)
  discardByTrackingCodeFilterDataFrame.show(false)

  println("discardByTrackingCodeWindow")
  val discardByTrackingCodeWindowDataFrame = FilterFunctions.discardByTrackingCodeWindow(visits)
  discardByTrackingCodeWindowDataFrame.show(false)
}

object FilterFunctions{

  def discardByTrackingCodeJoin(rawDataFrame: DataFrame): DataFrame = {
    val visitWithColumn = rawDataFrame.
      withColumn("tracking_version",
        expr("substring(path, 38, 1)"))
    val itemVersionDf = visitWithColumn.
      withColumn("item_version",
        concat(col("ItemId"), lit("_"), col("tracking_version")))
    val versionToTakeDf = itemVersionDf.
      groupBy(col("ItemId").as("item_id_delete")).
      agg(max("item_version").as("item_version"))
    val itemReport = itemVersionDf.join(versionToTakeDf, Seq("item_version"))
    val finalDf = itemReport.select("ItemId", "Visits", "item_version")
    finalDf
  }

  def discardByTrackingCodeFilter(rawDataFrame:DataFrame):DataFrame={
    val visitWithColumn = rawDataFrame.withColumn("tracking_version", expr("substring(path, 38, 1)"))
    val maxValueA = visitWithColumn.agg(max("tracking_version")).collect()(0)(0).asInstanceOf[String].toInt
    val finalDf= visitWithColumn.filter(col("tracking_version")===maxValueA).select("ItemId", "Visits")
    finalDf
  }

  def discardByTrackingCodeWindow(rawDataFrame:DataFrame):DataFrame={

    val window = Window.partitionBy("itemidnumber").orderBy(desc("fileVersion"))

    rawDataFrame.toDF("itemId", "visits", "path")
      .withColumn("itemidnumber", expr("substring(itemId, 5, 4)"))
      .withColumn("fileversion", expr("substring(path, 38, 1)"))
      .withColumn("tracking_version", expr("concat(itemidnumber, substring(path, 38, 1))"))
      .withColumn("row_number", row_number.over(window))
      .filter(col("row_number") === 1).select("itemId", "visits")
  }

}