package testsession

object TestSession extends App with SparkWrapper {

  import sparkSession.implicits._

  val df = Seq(
    (0, "2016-07-02 12:01:40"),
    (1, "2016-07-02 12:21:23"),
    (1, "2016-07-02 13:22:56"),
    (1, "2016-07-02 13:27:07"),
    (0, "2016-07-02 13:30:12"),
    (0, "2016-07-02 13:40:34"),
    (1, "2016-07-02 13:57:07"),
    (1, "2016-07-02 14:08:07")
  ).toDF("signal", "timestamp")

  df.show()

}
