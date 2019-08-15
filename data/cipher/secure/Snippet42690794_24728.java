object RSA {
  def decodePublicKey(encodedKey: String):Option[PublicKey] = { 
    this.decodePublicKey(
      (new Base64()).decode(encodedKey)
    )   
  }
  def decodePublicKey(encodedKey: Array[Byte]): Option[PublicKey]= { 
    scala.util.control.Exception.allCatch.opt {
      val spec = new X509EncodedKeySpec(encodedKey)
      val factory = KeyFactory.getInstance("RSA")
      factory.generatePublic(spec)
    }   
  }
  def encrypt(data: String,key:PublicKey): Array[Byte] = { 
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val file=Source.fromFile(data)
    val stringWords=file.toArray
    val result=new String(stringWords)
    val words=result.getBytes()
    cipher.doFinal(words)
    }
  def main(args:Array[String]):Unit={
    println("The keys of RSA " + decodePublicKey("")
    val publicKey=decodePublicKey("")
    val cipher = encrypt("D:\\text.txt",publicKey.get)
    println("cipher is"+ cipher)
  }
}
