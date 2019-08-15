private static String encrypt(String encryptionKey, String value) throws Exception{

    //Instantiate the encrypter/decrypter
    String paddedValue = value;
    if ((value.length() % 8) != 0) {
        for (int i= 0; i< (8 - value.length() % 8); i++) {
            paddedValue += ' ';
        }
    }             

    byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    byte[] valueByteArray = paddedValue.getBytes("UTF8");

    SecretKeySpec desKey = new SecretKeySpec(encryptionKey.getBytes(), "DES");
    Cipher desCipher = Cipher.getInstance("DES/CBC/NoPadding");
    desCipher.init(Cipher.ENCRYPT_MODE, desKey, new IvParameterSpec(iv));

    // Encode the string into bytes using utf-8        
    // Encrypt
    byte[] encryptedBytesArray = desCipher.doFinal(valueByteArray);

    StringBuffer encryptedValue = new StringBuffer(1000000);

    for (int i = 0, n = encryptedBytesArray.length; i < n; i++) {
        String hex = (java.lang.Integer.toHexString(encryptedBytesArray[i] & 0XFF));
        if (hex.length() == 1) {
            encryptedValue.append("0").append(hex);
        } else {
            encryptedValue.append(hex);
        }
    }

    return encryptedValue.toString().toUpperCase();
}
