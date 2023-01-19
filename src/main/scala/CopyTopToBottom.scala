import spark.Tokenized.sparkSession
import testsession.SparkWrapper

object CopyTopToBottom extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  println("Start")

  import org.apache.spark.sql.expressions.Window

  val df = Seq(
    (1,1, null, null, null ),
    (2,1, null, null, null ),
    (3,1, null, null, null ),
    (4,1,"123.12", "124.52", "95.98" ),
    (5,1, "452.12", "478.65", "1865.12" ),
    (1,2,"2014.21", "147", "265"),
    (2,2, "1457", "12483.00", "215.21"),
    (null, null, null, null),
    (4,2, null, null, null) ).toDF("index", "category", "col1", "col2", "col3")

  df.show

  val cols = Seq("col1", "col2", "col3")

  val beforeWindow = Window
    .partitionBy("category")
    .orderBy("index")
    .rangeBetween(Window.unboundedPreceding, Window.currentRow)

  val afterWindow = Window
    .partitionBy("category")
    .orderBy("index")
    .rangeBetween(Window.currentRow, Window.unboundedFollowing)

  val result = cols.foldLeft(df)((updated, columnName) =>
    updated.withColumn(columnName,
      coalesce(col(columnName),
        last(columnName, ignoreNulls = true).over(beforeWindow),
        first(columnName, ignoreNulls = true).over(afterWindow)
      ))
  )

  result.show(false)
}