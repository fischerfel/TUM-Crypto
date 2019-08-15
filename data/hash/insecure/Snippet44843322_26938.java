import java.security.MessageDigest

val md5 = udf((string: String) => {MessageDigest.getInstance("MD5").digest(string.getBytes).map("%02X".format(_)).mkString + scala.util.Random.alphanumeric.take(10).mkString })

df = df.withColumn("uuid",md5(col("message")))
