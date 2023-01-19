package spark

import testsession.TestSession.sparkSession
import testsession.SparkWrapper

object GroupByTwoOptions extends App with SparkWrapper {
  import sparkSession.implicits._

  val df = Seq(
    (1, 1, "m10", "t22"),
    (1, 2, "m10", "t22"),
    (1, 3, "m11", "t22"),
    (1, 4, "m11", "t22"),
    (1, 5, "m10", "t22"),
    (1, 6, "m10", "t22"),
    (1, 7, "m10", "t22"),
    (1, 8, "m11", "t22"),
    (1, 9, "m10", "t22"),
    (1, 10, "m10", "t22"),
    (2, 1, "m10", "t22"),
    (2, 2, "m11", "t22"),
    (2, 3, "m10", "t22"),
    (2, 4, "m10", "t22"),
    (2, 5, "m10", "t22"),
    (2, 9, "m10", "t22"),
    (2, 10, "m11", "t22"),
    (3, 4, "m10", "t22"),
    (3, 5, "m11", "t22"),
    (3, 6, "m10", "t22"),
    (3, 7, "m10", "t22"),
    (3, 8, "m10", "t22"),
    (3, 9, "m11", "t22"),
    (3, 10, "m10", "t22")).toDF("org_id", "rule_id", "period_id", "base_id")


  df.show(50, false)

  val df2 = df.orderBy("org_id", "rule_id")

  df2.show(50, false)

}
