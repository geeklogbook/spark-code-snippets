from pyspark.sql.functions import col
import os
import sys
from pyspark.sql import SparkSession

os.environ['PYSPARK_PYTHON'] = sys.executable
os.environ['PYSPARK_DRIVER_PYTHON'] = sys.executable
spark = SparkSession.builder.getOrCreate()


df = spark.read.json("./data/attributesSimple.json")
df.printSchema()
df.show()

dfFlatten = df.withColumn("attributes_id", col(
    "attributes.id")).drop("attributes")

dfFlatten.printSchema()
dfFlatten.show()
