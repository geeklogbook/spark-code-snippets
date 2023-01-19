package spark

import testsession.SparkWrapper


object Tokenized extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  val tokenized = Seq(
    (1, Array("Hi there", "Hello There")),
    (2, Array("Bye now")),
    (3, Array("Thank You", "Thanks", "Many thanks"))
  ).toDF("id", "sentences")

  tokenized.show()

  val result = tokenized
    .select($"id", posexplode($"sentences"))
    .select(concat($"id", lit(":"), $"pos") as "docID", $"col" as "sentences")

  result.show
}

