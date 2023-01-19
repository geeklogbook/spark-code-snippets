package spark

import testsession.SparkWrapper

object FillNullColumn extends App with SparkWrapper{
  import sparkSession.implicits._

  val df = Seq(
    (0, null, "rojo"),
    (1, "2016-07-02 12:21:23", "rojo"),
    (5, "2016-07-02 13:22:56","rojo"),
    (1, "2016-07-02 13:27:07", null),
    (0, "2016-07-02 13:30:12","rojo"),
    (0, null,"rojo"),
    (1, "2016-07-02 13:57:07",null),
    (1, "2016-07-02 14:08:07","rojo")).toDF("signal", "timestamp", "color")

   val map = Map("timestamp" -> "SIN TIEMPO", "color" -> "SIN COLOR")
   val withoutNulls = df.na.fill(map)

  df.show()
  withoutNulls.show()
}
