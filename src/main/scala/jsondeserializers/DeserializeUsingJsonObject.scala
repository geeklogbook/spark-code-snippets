package spark.jsondeserializers

import testsession.SparkWrapper

object DeserializeUsingJsonObject extends App with SparkWrapper {

  import sparkSession.implicits._

  val df = Seq(
    ("p1", """{"a.id": 1, "b": 2}"""),
    ("p2", """{"a.id": 3}""")).toDF("p_id", "p_meta")

  df.printSchema()
  df.show(false)



}
