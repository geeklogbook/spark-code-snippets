package spark

import testsession.SparkWrapper

object GroupingByBefore extends App with SparkWrapper{
  import org.apache.spark.sql.expressions.Window
  import org.apache.spark.sql.functions
  import org.apache.spark.sql.functions._
  import sparkSession.implicits._

  val df = Seq((0, "2016-07-02 12:01:40"),
   (1, "2016-07-02 12:21:23"),
   (1, "2016-07-02 13:22:56"),
   (1, "2016-07-02 13:27:07"),
   (0, "2016-07-02 13:30:12"),
   (0, "2016-07-02 13:40:34"),
   (1, "2016-07-02 13:57:07"),
   (1, "2016-07-02 14:08:07")).
  toDF("signal", "timestamp").
  withColumn("timestamp", functions.to_timestamp(col("timestamp"), "yyyy-MM-dd HH:mm:ss"))
  df.show()

  val newSignalWindow = Window.orderBy("timestamp")

  val dfWithNewSignal = df.withColumn("new_signal",
                                      (functions.lag(col("signal"),1,0).over(newSignalWindow) === 0 &&
                                       col("signal") === 1).cast("bigint"))

  dfWithNewSignal.show

  val dfWithIdSignal = dfWithNewSignal.
                              filter(col("signal") === 1).
                              withColumn("new_signal",
                                         functions.
                                            sum("new_signal").
                                            over(newSignalWindow))

  dfWithIdSignal.show

  val resultDF = dfWithIdSignal.
                    groupBy("new_signal").
                        agg(functions.min("timestamp").as("start_date"), functions.max("timestamp").as("end_date"),
                            functions.count("*").as("positive_count")).drop("new_signal")

  resultDF.show

}
