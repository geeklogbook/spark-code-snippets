import testsession.SparkWrapper

object GroupWithClause extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  println("start")

  import org.apache.spark.sql.Row
  import org.apache.spark.sql.types.StructType



  val a = Seq(
              ("10", "MILLER", "1300", "2017-11-03"),
              ("30", "Martin", "1250", "2017-11-21")
             ).toDF("dept_no","emp_name","sal","date")

  a.show

  val lst = List("10", "Susan").toDF

  val data = a.rdd.zip(lst.rdd).map { case (l, r) => Row.fromSeq(l.toSeq ++ r.toSeq) }
  val schema = StructType(a.schema.fields ++ lst.schema.fields)


  var df = Seq(
    ("TAG1", "2019-06-21 01:16:00.0",621.0947),
    ("TAG1", "2019-06-21 01:16:00.0",621.0947),
    ("TAG1", "2019-06-21 01:16:00.0",621.0947),
    ("TAG1", "2019-06-21 01:16:00.0",619.9578),
    ("TAG2", "2019-06-21 01:29:00.0",767.5475),
    ("TAG2", "2019-06-21 01:29:00.0",768.9506),
    ("TAG2", "2019-06-21 01:29:00.0",770.8863), //Map(Tag1 -> avg, Tag2 -> max, Tag3 -> min)
    ("TAG3", "2019-06-21 01:16:00.0",203.7457)).toDF("TAG","Timestamp","Value")

  val dif_aggregation = df.groupBy(
      "TAG", "Timestamp"
  ).agg(
      when(
          col("TAG") === "TAG1", avg("Value")
      ).otherwise(
          when(col("TAG") === "TAG2", max("Value")).otherwise(min("Value"))
      ).as("Value"))

  dif_aggregation.show



}



