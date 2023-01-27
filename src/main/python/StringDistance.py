from pyspark.sql.functions import lower, col, regexp_replace, udf, levenshtein, lit, expr, when
from pyspark.sql.types import *
import os
import sys
from pyspark.sql import SparkSession

os.environ['PYSPARK_PYTHON'] = sys.executable
os.environ['PYSPARK_DRIVER_PYTHON'] = sys.executable
spark = SparkSession.builder.getOrCreate()



df = spark.createDataFrame(
    [(1, 33, 'las tres hermanas'),
     (2, 45, 'treshermanas'),
     (3, 12, 'tres hermanas'),
     (4, 14, 'la tres hermana'),
     (5, 73, '3hermanas'),
     (6, 62, 'las tres herman'),
     (7, 14, 'la tres HERMANAS'),
     (8, 14, 'son todas diferntes'),
     (10, 12, 'tres tristes tigres'),], ['id', 'cantidad', 'marca'])

df.show()
"""
+---+--------+-------------------+
| id|cantidad|              marca|
+---+--------+-------------------+
|  1|      33|  las tres hermanas|
|  2|      45|       treshermanas|
|  3|      12|      tres hermanas|
|  4|      14|    la tres hermana|
|  5|      73|          3hermanas|
|  6|      62|    las tres herman|
|  7|      14|   la tres HERMANAS|
|  8|      14|son todas diferntes|
| 10|      12|tres tristes tigres|
+---+--------+-------------------+
"""

def anonimization(dataframe, marca):
    marca_control = marca.lower().replace(" ", "")
    stringDistanceDf = dataframe. \
        withColumn("marca_limpia", regexp_replace(lower(col("marca")), " ", "")). \
        withColumn("control_str", lit(marca_control)). \
        withColumn("string_distance", levenshtein(col("marca_limpia"), col("control_str")))

    new_column_2 = when(col("string_distance") <= 7 , lit("marca")).otherwise(lit("desconocido"))
    finalDf = stringDistanceDf. \
        withColumn("marca_anom", new_column_2). \
        drop("marca","marca_limpia","control_str","string_distance")
    return finalDf

marca = "LAS TRES HERMANAS"
testDf = anonimization(df, marca)
testDf.show()
"""
+---+--------+-----------+
| id|cantidad| marca_anom|
+---+--------+-----------+
|  1|      33|      marca|
|  2|      45|      marca|
|  3|      12|      marca|
|  4|      14|      marca|
|  5|      73|      marca|
|  6|      62|      marca|
|  7|      14|      marca|
|  8|      14|desconocido|
| 10|      12|desconocido|
+---+--------+-----------+
"""