  def ivParameterSpec = this.synchronized{
    import com.schedule1.datapassport.view._

    new IvParameterSpec("DataPassports===")
  }

  private def getCipher = this.synchronized {
    Cipher.getInstance("AES/CBC/PKCS5Padding")
  }

  private def nextCipher(aesKey: Key): Cipher = this.synchronized{
    val cipher = getCipher
    cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec)
    cipher
  }

  private def nextDecipher(aesKey: Key): Cipher = this.synchronized{
    val cipher = getCipher
    cipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec)
    cipher
  }

  def nullBytes = Array.fill[Byte](16)(0)

  def aesEncrypt(bytes: Array[Byte], key: Key): Array[Byte] = this.synchronized{
    val effectiveBytes = if (bytes == null) nullBytes
    else bytes
    nextCipher(key).doFinal(effectiveBytes)
  }

  def aesDecrypt(cipher: Array[Byte], key: Key): Array[Byte] = this.synchronized{
    val effectiveBytes = Utils.retry(3){
      nextDecipher(key).doFinal(cipher)
    }
    if (effectiveBytes.toList == nullBytes.toList) null
    else effectiveBytes
  }
