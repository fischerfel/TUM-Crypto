  def encryptData(data: String) = {
    val saltBytes = Array[Int](0x12, 0x34, 0x56, 0x78, 0x12, 0x34, 0x56, 0x78, 0x78, 0x90, 0x12, 0x34, 0x90, 0x12, 0x34, 0x56) map {_.asInstanceOf[Byte]}
    val ivBytes = Array[Int](0x12, 0x34, 0x56, 0x78, 0x90, 0xab, 0xcd, 0xef, 0x12, 0x34, 0x56, 0x78, 0x90, 0x12, 0x34, 0x56) map {_.asInstanceOf[Byte]}
    val ivParamSpec = new IvParameterSpec(ivBytes)
    val secretKey = genSecretKey("somekey", saltBytes)
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec)
    val encryptedData = cipher.doFinal(data.getBytes("UTF-8"))
    val encodedData = (new BASE64Encoder()) encodeBuffer(encryptedData)
    Json.obj(
              "salt" -> DatatypeConverter.printHexBinary(saltBytes),
              "iv" -> DatatypeConverter.printHexBinary(ivBytes),
              "data" -> encodedData
            )
  }

  def decryptData(encryptedData: EncryptedData) = {
    val saltBytes = DatatypeConverter.parseHexBinary(encryptedData.salt)
    val ivBytes = DatatypeConverter.parseHexBinary(encryptedData.iv)
    val ivParamSpec = new IvParameterSpec(ivBytes)
    val secretKey = genSecretKey("somekey", saltBytes)
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParamSpec)
    val decodedValue = (new BASE64Decoder()) decodeBuffer(encryptedData.data)
    val decryptedData = new String(cipher.doFinal(decodedValue))
    decryptedData
  }
