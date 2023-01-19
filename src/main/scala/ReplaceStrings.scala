import spark.Tokenized.sparkSession
import testsession.SparkWrapper

object ReplaceStrings extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  println("Start")

  import org.apache.spark.sql.functions._

  val df = Seq("12 https://jhafi.com xyza@gmail.com",
    "12 https://jhafi.com xyza@gmail.com",
    "12 https://jhafi.com xyza@gmail.com",
    "12 https://jhafi.com xyza@gmail.com").toDF("mycol")

  df.show()

  val email = """(?i)\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b"""
  val url ="(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})"

  val urlRegexExpression = regexp_replace($"mycol", url, "PHTEXTURL")
  val emailRegexExpression = regexp_replace($"mycol", email, "PHTEXTEMAIL")

  df.withColumn("mycol", emailRegexExpression).withColumn("mycol", urlRegexExpression).show(false)

}
