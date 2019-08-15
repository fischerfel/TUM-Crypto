scala> import java.security.MessageDigest
import java.security.MessageDigest

scala> MessageDigest.getInstance("MD5").digest("1".getBytes)
res0: Array[Byte] = Array(-60, -54, 66, 56, -96, -71, 35, -126, 13, -52, 80, -102, 111, 117, -124, -101)

scala> val hash = MessageDigest.getInstance("MD5").digest("1".getBytes("UTF-8")).length
hash: Int = 16
