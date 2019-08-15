import org.apache.spark.sql.functions._
def toHex(bytes: Array[Byte]): String = bytes.map("%02x".format(_)).mkString("")
def md5 = udf((s: String) => toHex(MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"))))

val new_df = load_df.withColumn("New_MD5_Column", md5(col("Duration")))
