package spark.filter

import org.apache.spark.sql.functions.col
import testsession.SparkWrapper

object FilterByList extends App with SparkWrapper{
  import sparkSession.implicits._

  val friendsRawDf = Seq(
    ("Rahul", "99", "AA"),
    ("Rahul", "20", "BB"),//
    ("Rahul", "30", "BB"),
    ("Mahesh", "55", "CC"),//
    ("Mahesh", "88", "DD"),
    ("Mahesh", "44", "FF"),
    ("Ramu", "30", "FF"),//
    ("Gaurav", "20", "HH"),//
    ("Gaurav", "57", "CC")).toDF("Name", "Age", "City")

  friendsRawDf.printSchema()
  friendsRawDf.show(false)

  val namesToFilter = Seq("Rahul", "Mahesh", "Gaurav")
  val ageToFilter = List(20,55)

  val filteredDf = friendsRawDf.
    filter(col("Name").isin(namesToFilter:_*) &&
            col("Age").isin(ageToFilter:_*))

  filteredDf.printSchema()
  filteredDf.show(false)

}
