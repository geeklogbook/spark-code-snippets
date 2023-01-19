package spark.filter

import testsession.SparkWrapper

object FilterAllRow extends App with SparkWrapper {

  import sparkSession.implicits._

  println("start")

  import org.apache.spark.sql.expressions.Window
  import org.apache.spark.sql.functions._

  val example_df = Seq(
    ("id_1", "20190601", "title_1", "junin", "yes"),
    ("id_1", "20190601", "title_1", "rivadavia", "yes"),
    ("id_1", "20190602", "title_1", "caleta olivia", "no"),
    ("id_1", "20190603", "title_1", "ensenada", "yes"),
    ("id_1", "20190604", "title_1", "junin", "no"),
    ("id_1", "20190605", "title_1", "rivadavia", "yes"),
    ("id_1", "20190606", "title_2", "caleta olivia", "no"),
    ("id_1", "20190607", "title_2", "ensenada", "yes"),
    ("id_1", "20190608", "title_2", "junin", "yes"),
    ("id_1", "20190609", "title_2", "rivadavia", "yes"),
    ("id_1", "20190610", "title_2", "caleta olivia", "no"),
    ("id_2", "20190601", "title_1", "ensenada", "yes"),
    ("id_2", "20190602", "title_1", "junin", "no"),
    ("id_2", "20190603", "title_1", "rivadavia", "yes"),
    ("id_2", "20190604", "title_1", "caleta olivia", "no"),
    ("id_2", "20190605", "title_1", "ensenada", "yes"),
    ("id_2", "20190606", "title_1", "junin", "yes"),
    ("id_2", "20190607", "title_2", "rivadavia", "yes"),
    ("id_2", "20190608", "title_2", "caleta olivia", "no"),
    ("id_2", "20190609", "title_2", "ensenada", "yes"),
    ("id_2", "20190610", "title_2", "junin", "no"),
    ("id_3", "20190601", "title_2", "rivadavia", "yes"),
    ("id_3", "20190602", "title_2", "caleta olivia", "no"),
    ("id_3", "20190603", "title_2", "ensenada", "yes"),
    ("id_3", "20190604", "title_2", "junin", "yes"),
    ("id_3", "20190605", "title_2", "rivadavia", "yes"),
    ("id_3", "20190606", "title_1", "caleta olivia", "no"),
    ("id_3", "20190607", "title_1", "ensenada", "yes"),
    ("id_3", "20190608", "title_1", "junin", "no"),
    ("id_3", "20190609", "title_1", "rivadavia", "yes"),
    ("id_3", "20190610", "title_2", "caleta olivia", "no"),
    ("id_4", "20190601", "title_1", "ensenada", "yes"),
    ("id_4", "20190602", "title_1", "junin", "yes"),
    ("id_4", "20190603", "title_1", "rivadavia", "yes"),
    ("id_4", "20190604", "title_2", "caleta olivia", "no"),
    ("id_4", "20190605", "title_2", "ensenada", "yes"),
    ("id_4", "20190606", "title_2", "junin", "no"),
    ("id_4", "20190607", "title_2", "rivadavia", "yes"),
    ("id_4", "20190608", "title_1", "caleta olivia", "no"),
    ("id_4", "20190609", "title_1", "ensenada", "yes"),
    ("id_4", "20190610", "title_1", "junin", "yes"),
    ("id_5", "20190601", "title_1", "rivadavia", "yes"),
    ("id_5", "20190602", "title_2", "caleta olivia", "no"),
    ("id_5", "20190603", "title_2", "ensenada", "yes"),
    ("id_5", "20190604", "title_2", "junin", "no"),
    ("id_5", "20190605", "title_2", "rivadavia", "yes"),
    ("id_5", "20190606", "title_2", "caleta olivia", "no"),
    ("id_5", "20190607", "title_2", "ensenada", "yes"),
    ("id_5", "20190608", "title_1", "junin", "yes"),
    ("id_5", "20190609", "title_1", "rivadavia", "yes"),
    ("id_5", "20190610", "title_1", "caleta olivia", "no")
  ).toDF("id", "datetrack", "title", "address_city", "oficial_store")

  example_df.show

  val test_df = example_df

  test_df.show

  val grouped_id = example_df.
    groupBy("id", "title").count()

  val window = Window.partitionBy("id").orderBy(desc("count"))
  val order_df = grouped_id.withColumn("order", row_number().over(window))

  order_df.orderBy("id", "title").show


}
