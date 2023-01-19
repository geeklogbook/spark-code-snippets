package spark

import testsession.SparkWrapper

object Joins extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions.explode
  import org.apache.spark.sql.functions._

  println("Start")

  import org.apache.spark.sql.expressions.Window
  import org.apache.spark.sql.functions._

  val left = Seq((0, "zero"), (1, "one")).toDF("id", "left")
  val right = Seq((0, "zero"), (2, "two"), (3, "three")).toDF("id", "right")

  left.show

  right.show

  left.join(right, "id").show

  val dfTries = Seq(
    ("Try 1", "2018-08-01"),
    ("Try 2", "2018-09-01"),
    ("Try 3", "2018-10-01"),
    ("Try 4", "2018-10-02")
  ).toDF("try", "try_day")

  val dfContracts = Seq(
    ("contract 1", "2018-08-01"),
    ("contract 2", "2018-09-02"),
    ("contract 3", "2018-10-01")
  ).toDF("contract", "contract_day")

  dfTries.
    withColumn("try_ts", unix_timestamp($"try_day", "yyyy-MM-dd")).
    withColumn("prev_try_ts", lead($"try_ts",1).over(Window.orderBy($"try"))).
    withColumn("rank", when(
      $"prev_try_ts".isNull || abs($"try_ts" - $"prev_try_ts") > 10 * 24 * 3600,
      1
    ).otherwise(2)
    ).
    join(
      dfContracts,
      $"rank" === 1 && abs($"try_ts" - unix_timestamp($"contract_day", "yyyy-MM-dd")) <= 2 * 24 * 3600,
      "left_outer").show
}


