package spark

import testsession.SparkWrapper

object TablasSpark extends SparkWrapper{
  import sparkSession.implicits._

  def main(args: Array[String]) {
    val df1 = Seq("12 https://jhafi.com xyza@gmail.com",
      "12 https://jhafi.com xyza@gmail.com",
      "12 https://jhafi.com xyza@gmail.com",
      "12 https://jhafi.com xyza@gmail.com").toDF("mycol")
    var fieldStr_df1 = GetSchema.get_schema(df1)
    println(fieldStr_df1)

    val df2 = Seq((0, "2016-07-02 12:01:40"),
      (1, "2016-07-02 12:21:23"),
      (1, "2016-07-02 13:22:56"),
      (1, "2016-07-02 13:27:07"),
      (0, "2016-07-02 13:30:12"),
      (0, "2016-07-02 13:40:34"),
      (1, "2016-07-02 13:57:07"),
      (1, "2016-07-02 14:08:07")).
      toDF("signal", "timestamp")
    var fieldStr_df2 = GetSchema.get_schema(df2)
    println(fieldStr_df2)
  }
}

object GetSchema {
  // acomoda campos
  def get_schema(Dat: org.apache.spark.sql.DataFrame): String = {
    var fieldStr ="";
    Dat.schema.fields.foreach{f =>
      fieldStr += f.name + " " + f.dataType.simpleString + ",";
    }
    return fieldStr
  }
}