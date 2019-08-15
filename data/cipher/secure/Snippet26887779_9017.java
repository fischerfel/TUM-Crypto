/*Encrypt the password using public key.public key is obtained from generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent) function)*/

public static String rsaEncrypt(String originalString, PublicKey key) {
    try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherByte = cipher.doFinal(original);
        return bytesToHex(cipherByte);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

//generate public key with given module and exponent value
public static PublicKey generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent) {
    PublicKey key = null;
    try {
        key = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
      return key;
    } catch (Exception e) {
        Log.e("error", e.toString());
        // return null;
    }
    return key;
}

// Helper methods

final protected static char[] hexArray = "0123456789abcdef".toCharArray();
public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
       // Log.d("byte array representaion","value in integrer"+v);
        hexChars[j * 2] = hexArray[v >>> 4];           
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
