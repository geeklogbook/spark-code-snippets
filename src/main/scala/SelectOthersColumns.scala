package spark

import testsession.SparkWrapper

object SelectOthersColumns extends SparkWrapper {
  import sparkSession.implicits._


  def main(args: Array[String]): Unit = {
    val df = Seq(
      (1, 10, 0),
      (1, 11, 1),
      (1, 13, 1),
      (1, 34, 1),
      (1, 35, 1),
      (1, 38, 0)).toDF("Category", "Value", "Sequences")
    df.show

    println(df.columns)

    val test = df.columns

    val columns_to_get = Seq("Category", "Value")
    val columnsRequired = test.filter(columns_to_get.contains(_))
    val columnsToDelete = test.filter(!columns_to_get.contains(_))

    columnsRequired.foreach(println)
    columnsToDelete.foreach(println)

  }
}
