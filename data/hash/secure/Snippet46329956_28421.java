  val digest = MessageDigest.getInstance("SHA-256");      
  private def getCheckSum() = {
    println(new String(digest.digest(("Some String").getBytes(StandardCharsets.UTF_8))))        
  }
