package spark

import spark.JoinsForCountry.sparkSession
import org.apache.spark.sql.functions._
import testsession.SparkWrapper

object PivotDimension extends App with SparkWrapper{

  import sparkSession.implicits._

  println("Start")

  val sellerAndCategory = Seq(
    ("ID12", "CAT1"),
    ("ID13", "CAT1"),
    ("ID14", "CAT8"),
    ("ID13", "CAT2"),
    ("ID14", "CAT3"),
    ("ID12", "CAT4"),
    ("ID12", "CAT5"),
    ("ID12", "CAT6"),
    ("ID12", "CAT7"),
    ("ID12", "CAT8")
  ).toDF("seller_id", "category_id")

  val pivotedDf = sellerAndCategory.groupBy("seller_id").
                                          pivot("category_id").
                                          agg(first("category_id"))

  //sellerAndCategory.show(false)
  pivotedDf.show(false)

}
