  def getHash(key:Array[Byte]): String = {
    try
    {
      val md = MessageDigest.getInstance("SHA-256").digest(key)
      md.map("%02x".format(_)).mkString.toLowerCase()
    }
    catch
      {
        case e: Exception => ""
      }
  }
