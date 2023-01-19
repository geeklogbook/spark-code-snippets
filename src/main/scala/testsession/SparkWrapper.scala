package testsession

import org.apache.spark.sql.SparkSession

trait SparkWrapper {

  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("scala-utilities")
      .master("local")
      .getOrCreate()

}