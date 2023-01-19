package spark

import testsession.SparkWrapper

object ConvertTimestamp extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  val df = Seq(
    //("Thu Sep 12 2019 15:58:30 GMT-0500 (hora estándar de Colombia)")
      ("Thu Sep 12 2019 15:58:30")
  ).toDF("Date")

  val df2 = df.withColumn("timestamp_raw", regexp_replace(col("Date"), "(hora estándar de Colombia)", "" ))
  val df3 = df2.withColumn("timestamp",to_timestamp(col("timestamp_raw"), "dd/MM/yy HH:mm"))
  //val df3 = df2.withColumn("timestamp", col("timestamp_raw").cast("timestamp"))

  df3.show(20, false)
}
