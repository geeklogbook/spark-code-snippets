package spark.jsondeserializers

import testsession.SparkWrapper

object FlattenJson extends App with SparkWrapper{

  val jsonToFlattenDf = sparkSession.read.json("C:\\Users\\User\\Documents\\varios\\utilities\\scala_utilities\\src\\main\\scala\\spark\\exampledatafiles\\jsonToDeserialize.json")

  jsonToFlattenDf.printSchema()
  jsonToFlattenDf.show(false)
}
