byte[] decryptData(byte[] data, byte[] key, byte[] iv)
{
    static int FBENCRYPT_BLOCK_SIZE = 16; //(kCCBlockSizeAES128) 
    static int FBENCRYPT_KEY_SIZE = 32; //(kCCKeySizeAES256)        
    // setup key
    byte[] cKey = new byte[FBENCRYPT_KEY_SIZE];
    Arrays.fill(cKey, (byte) 0x00);

    int num = FBENCRYPT_KEY_SIZE;
    if (key.length<num)
        num = key.length;
    System.arraycopy(key, 0, cKey, 0, num);

    // setup iv
    byte[] cIv = new byte[FBENCRYPT_BLOCK_SIZE];
    Arrays.fill(cIv, (byte) 0x00);
    if (iv!=null) {
        System.arraycopy(iv, 0, cIv, 0, iv.length);
    } 

    Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    SecretKeySpec skeySpec = new SecretKeySpec(cKey, "AES");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(cIv);
    aesCipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
    byte[] byteCipherText = aesCipher.doFinal(data);
    return byteCipherText;
}
