package dates

import testsession.SparkWrapper
import org.apache.spark.sql.functions.udf

object ConvertToJulianDate extends SparkWrapper with App{

  import sparkSession.implicits._

  def convertToJulian(str:String): Long = {
    val julianDate = java.time.LocalDate.parse(str).
      getLong(java.time.temporal.JulianFields.JULIAN_DAY)

    julianDate
    }

  //val toJulianFunction = udf(_:String)

  println(convertToJulian("2009-01-15"))

  val toJulianFunctionFunctional =
    udf(
      (str:String)
          => java.time.LocalDate.parse(str).
                getLong(java.time.temporal.JulianFields.JULIAN_DAY))

  val originDf = Seq("2009-01-15").toDF("date")

  originDf.show(false)

  val toJulianDf = originDf.withColumn("julian_date", toJulianFunctionFunctional($"date"))

  toJulianDf.show(false)

}
