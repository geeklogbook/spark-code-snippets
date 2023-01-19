package spark

import testsession.TestSession.sparkSession


object PivotingWithClause extends App {

  import sparkSession.implicits._
  import org.apache.spark.sql.functions._

  val dfToPivot = Seq(
    ("20200411", "DIM_2", 	57	,1	,6),
    ("20200413", "DIM_2", 	5	,95	,43),
    ("20200412", "DIM_2", 	98	,91	,84),
    ("20200411", "DIM_2", 	56	,65	,72),
    ("20200407", "DIM_2", 	21	,27	,60),
    ("20200410", "DIM_1", 	26	,18	,63),
    ("20200405", "DIM_1", 	77	,43	,77),
    ("20200411", "DIM_1", 	94	,68	,80),
    ("20200406", "DIM_1", 	4	,7	,49),
    ("20200408", "DIM_1", 	32	,55	,65),
    ("20200409", "DIM_3", 	31	,72	,35),
    ("20200402", "DIM_3", 	16	,34	,37),
    ("20200413", "DIM_3", 	95	,71	,78),
    ("20200405", "DIM_2", 	20	,42	,64),
    ("20200407", "DIM_1", 	80	,64	,4),
    ("20200405", "DIM_3", 	70	,26	,40),
    ("20200402", "DIM_3", 	26	,40	,26),
    ("20200401", "DIM_3", 	74	,53	,69),
    ("20200408", "DIM_3", 	21	,42	,45),
    ("20200408", "DIM_1", 	33	,62	,11),
    ("20200402", "DIM_2", 	90	,88	,82),
    ("20200407", "DIM_3", 	94	,66	,42),
    ("20200411", "DIM_3", 	24	,1	,27),
    ("20200412", "DIM_3", 	56	,1	,74)).toDF("date", "dimension", "metric1", "metric2", "metric3")

  dfToPivot.show()

  val dfPivoted = dfToPivot.groupBy("date").pivot("dimension", Seq("DIM_1", "DIM_2", "DIM_3"))

  val dfPivotedShow  = dfPivoted.agg(sum("metric1").as("Metric 1"),
                                     sum("metric2").as("Metric 2"),
                                     count("metric3").as("Metric 3"))

  dfPivotedShow.show()
}
