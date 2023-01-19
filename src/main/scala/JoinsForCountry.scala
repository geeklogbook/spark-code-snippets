package spark

import testsession.SparkWrapper


object JoinsForCountry extends App with SparkWrapper{
  import org.apache.spark.sql.functions._
  import sparkSession.implicits._

  println("Start")

  val paisesYContinentes = Seq(
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

  val paisesYContratosDataFrame = Seq(
    ("contract1", "PERU"),
    ("contract2", "COLOMBIA"),
    ("contract3", "COLOMBIA")
  ).toDF("contrato", "pais")


  //Tus queries
  val transform = paisesYContinentes.select(
                                      col("continente"),
                                      col("pais"))

  val condition = transform.where("continente = 'SUDAMERICA' AND (pais = 'PERU' OR pais = 'COLOMBIA')")

  condition.show(50,false)

  //Mis Queries
  println("transformConLista")
  // Transformo con la lista
  // Genero la lista con los países del data frame.
  val paises = paisesYContratosDataFrame.select("pais").collect().map(_(0)).toList

  val transformConLista = transform.
                        where(col("continente") === "SUDAMERICA").
                        where(col("pais") isin (paises:_*))
  transformConLista.show()

}


