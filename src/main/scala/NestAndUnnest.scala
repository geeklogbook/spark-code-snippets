import spark.Tokenized.sparkSession
import testsession.SparkWrapper

object NestAndUnnest extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._


  println("Start")

  val df = Seq(
    ("1005", "Miley", "John", "Spain", "Barceleona"),
    ("1009", "Rodney", "Justin", "France", "Paris"),
    ("1009", "Rodney", "Justin", "France", "Creteil")
  ).toDF("PERSON_ID", "LAST_NAME", "FIRST_NAME", "COUNTRY", "CITY")

  val newCols = List("id", "lastName", "name", "country",  "city")

  val resultDF = df.select(df.columns.zip(newCols).map(c => col(c._1).as(c._2)):_*)
                                  .groupBy("id", "name", "lastName")
                                  .agg(collect_list(struct($"city",$"country")).as("address"))


  resultDF.show

  resultDF.printSchema()


}
