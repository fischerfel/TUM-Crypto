  private static final byte[] IV = {
    0, 2, 4, 8, 16, 32, 64, 127, 
    127, 64, 32, 16, 8, 4, 2, 0
};

  //actual encryption over here
  private static byte[] encrypt(byte[] raw, byte[] clear) throws 
Exception {  
    SecretKeySpec skeySpec = new SecretKeySpec(raw,  "AES");  
    Cipher cipher = null;

    if(isIVUsedForCrypto) {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV));  
    }
    else 
    {
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  
    }
    byte[] encrypted = cipher.doFinal(clear);  
    return encrypted;  
}  
