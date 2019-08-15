def decode(input:String) = {
    val keyBytes = Hex.decodeHex("someKey".toCharArray)
    val inputWithoutPadding = input.substring(0,input.size - 2)
    val inputArr:Seq[Byte] = Hex.decodeHex(inputWithoutPadding.toCharArray)

    val skSpec = new SecretKeySpec(keyBytes, "AES")
    val iv = new IvParameterSpec(inputArr.slice(0,16).toArray)
    val dataToDecrypt = inputArr.slice(16,inputArr.size)

    val cipher = Cipher.getInstance("AES/CFB/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, skSpec, iv)
    cipher.doFinal(dataToDecrypt.toArray)
}
