package spark

import testsession.SparkWrapper


object SplitListAndAdded extends App with SparkWrapper{
  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  println("Start")

  val line = "Mon-18-June-2018,1:10:5,2:20:10,3:30:15,4:40:20,5:50:25"

  val arr = line.split(",")

  val map = scala.collection.mutable.Map[String,List[List[Int]]]()

  val key = arr(0)

  val values = arr.toList.drop(1).map{
    case str: String =>
      str.split(":").map(_.toInt).foldLeft(List[Int]())(
        (acc,res) =>
          acc :+ res
      )
  }

  map += (key -> values)

  val data = Seq(("One", 1),("Two", 2),("three", 3),("four", 4) ).toDF

  val columnMap = Map("col1" -> "val1", "col2" -> "val2")

  val newCols = columnMap.keys.map(k => lit(columnMap(k)) as k)

  data.select(data.columns.map(col) ++newCols: _*).show

}




