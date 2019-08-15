def md5Hash(input: String): String = md5HashArr(input.getBytes(StandardCharsets.UTF_8))
def md5HashArr(bytes: Array[Byte]): String = {
    val digest = java.security.MessageDigest.getInstance("MD5")
    digest.reset()
    digest.update(bytes)
    digest.digest().map(0xFF & _).map {
      "%02x".format(_)
    }.foldLeft("") {
      _ + _
    }
  }

def hex2dec(hex: String): BigInt = {
    hex.toLowerCase().toList.map(
      "0123456789abcdef".indexOf(_)).map(
      BigInt(_)).reduceLeft(_ * 16 + _)
 }

def bytes2hexStr(bytes: Array[Byte], sep: String = ""): String = bytes.map("0x%02x".format(_)).mkString(sep)


val result: BigInt = hex2dec(md5Hash("Data to pack"))
val resultAsByteArray: Array[Byte] = result.toByteArray
