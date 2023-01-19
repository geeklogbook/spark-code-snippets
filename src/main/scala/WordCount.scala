package spark

import testsession.SparkWrapper

object WordCount extends App with SparkWrapper{
  import org.apache.spark.sql.functions._
  import sparkSession.implicits._

  val df = Seq(
    (1, "4/8/2019", "CLM***120379893***John***CLM***Smith***blablabla**so..on…"),
    (2, "4/8/2019", "CLM***120379093***John***CLM***Smith***CLM***blablabla**so..on…"),
    (3, "4/8/2019", "CLM***139979893***John***Smith***blablabla**so..on…")
  ).toDF("id", "date", "content")

  def countAll(pattern: String) = udf((s: String) => pattern.r.findAllIn(s).size)

  df.withColumn("wordcount", countAll("CLM")($"content")).show

    val df1 = Seq(
      (1, "google.com"),
      (2, "facebook.com"),
      (3, "github.com"),
      (4, "stackoverflow.com")).toDF("id", "url").as("first")
    df1.show

    val df2 = Seq(
      (11, "google.com"),
      (12, "yahoo.com"),
      (13, "facebook.com"),
      (14, "twitter.com")).toDF("id", "url").as("second")
    df2.show

  import org.apache.spark.sql.functions.{col, _}

  val df3 = df2.join(df1, expr("first.url like second.url"), "full_outer")
                          .select(col("first.url"),col("first.url")
                                  .contains(col("second.url")).as("check"))
                                      .filter("url is not null")

  df3.show()

  df3.na.fill(Map("check" -> false)).show()
}
