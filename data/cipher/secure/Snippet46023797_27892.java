def decode(input:String, key:String) = {
    val cipher = Cipher.getInstance("AES/CFB/NoPadding")
    val blockSize = cipher.getBlockSize()
    val keyBytes = key.getBytes()
    val inputArr = Base64.getUrlDecoder().decode(input)
    val skSpec = new SecretKeySpec(keyBytes, "AES")
    val iv = new IvParameterSpec(inputArr.slice(0, blockSize).toArray)
    val dataToDecrypt = inputArr.slice(blockSize, inputArr.size)
    cipher.init(Cipher.DECRYPT_MODE, skSpec, iv)
    new String(cipher.doFinal(dataToDecrypt.toArray))
}

def main(args: Array[String]) {
    print(decode("c1bpFhxn74yzHQs-vgLcW6E5yL8zJfgceEQgYl0=", "0123456789abcdef"));
}
