import spark.Tokenized.sparkSession
import testsession.SparkWrapper

object UnitsByRange extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._
  import org.apache.spark.sql.expressions.Window

  val df = Seq(
    (1, 10, 0),
    (1, 11, 1),
    (1, 13, 1),
    (1, 16, 1),
    (1, 20, 0),
    (1, 21, 0),
    (1, 22, 1),
    (1, 25, 1),
    (1, 27, 1),
    (1, 29, 1),
    (1, 30, 0),
    (1, 32, 1),
    (1, 34, 1),
    (1, 35, 1),
    (1, 38, 0)).toDF("Category", "Value", "Sequences")
  df.show

  val zipped = df.withColumn("zip", monotonically_increasing_id())
  zipped.show

  val categoryWindow = Window.partitionBy("Category").orderBy($"zip")
  val groups = zipped
    .filter($"Sequences" === 0)
    .withColumn("rangeEnd", lead($"zip",1).over(categoryWindow))
    .withColumnRenamed("zip", "rangeStart")
  groups.show(false)

  val joinCondition = ($"units.zip" > $"groups.rangeStart").and($"units.zip" < $"groups.rangeEnd")

  val unitsByRange = zipped
    .filter($"Sequences" === 1).alias("units")
    .join(groups.alias("groups"), joinCondition, "left")
    .select("units.Category", "units.Value", "groups.rangeStart")

  unitsByRange.show

  val result = unitsByRange
    .groupBy($"Category", $"rangeStart")
    .agg(sum("Value").alias("summing"))
    .orderBy("rangeStart")
    .drop("rangeStart")

  result.show(false)
}
