scala> import java.security.MessageDigest
import java.security.MessageDigest

scala> import java.math.BigInteger
import java.math.BigInteger

scala> MessageDigest.getInstance("SHA-256").digest("some string".getBytes("UTF-8"))
res6: Array[Byte] = Array(97, -48, 52, 71, 49, 2, -41, -38, -61, 5, -112, 39, 112, 71, 31, -43, 15, 76, 91, 38, -10, -125, 26, 86, -35, -112, -75, 24, 75, 60, 48, -4)
