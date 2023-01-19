package spark

import testsession.SparkWrapper

object joinPaises extends SparkWrapper {

  def main(args: Array[String]): Unit = {
    import org.apache.spark.sql.functions._
    import sparkSession.implicits._

    val continentAndCountries = Seq(
      ("SUDAMERICA", "ARGENTINA"),
      ("EUROPA", "FRANCIA"),
      ("ASIA", "CHINA"),
      ("EUROPA", "ESPAÑA"),
      ("ASIA", "TAIWAN"),
      ("SUDAMERICA", "ECUADOR"),
      ("SUDAMERICA", "CHILE"),
      ("SUDAMERICA", "PERU"),
      ("SUDAMERICA", "COLOMBIA"),
      ("SUDAMERICA", "BRASIL")
    ).toDF("continente", "pais")

    val contractsAndCountries = Seq(
      ("contract1", "PERU"),
      ("contract2", "COLOMBIA"),
      ("contract3", "COLOMBIA")
    ).toDF("contrato", "pais")


    val joinByCountry = continentAndCountries.join(contractsAndCountries, Seq("pais", "pais"))

    joinByCountry.printSchema()

    joinByCountry.show()

  }


}
