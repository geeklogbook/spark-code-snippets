package spark

import testsession.SparkWrapper
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.functions._

object PercentageOverField extends App with SparkWrapper{
  import sparkSession.implicits._
  val df = Seq(("Resistor", 2),
               ("Capacitor",2),
               ("Inductor", 3)).toDF("Product", "countnum")
  df.show()

  val w = Window.orderBy("countnum").
                          rowsBetween(Window.unboundedPreceding,
                                      Window.unboundedFollowing)

  val finalDf = df.withColumn("overallprofit",
                              col("countnum")/sum(col("countnum")).
                              over(w)*100)

  finalDf.show()

}
