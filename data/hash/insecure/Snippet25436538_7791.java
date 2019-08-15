val hash: Array[Byte] = MessageDigest.getInstance("MD5").digest("Hello")
val number: java.math.BigInteger = new BigInteger(hash)
val bucket = number.mod(new BigInteger("1000"))
