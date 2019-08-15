private void exampleMethod(){
    String messageString = "This actually works in Java AND Android!";

    String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjS7T3WJ+VLucnUP5WYryeg+hhOjZZl5VxwvJAgo4GrXaXdernTPtmXnOSUlbhd928QRCip7D3rLwJNvGIwhPa6coA+YQnj+aHQC02AvCJP/9jpeNmm5MASZfYFXrdmOrMhAPpDZ4rUk1mqtvpwBkYmW3VbMtG336wT1bAIKPHCZuI2n6glupJvs8gK0NuIoAPRlxiQmQD7NCcRx1Et4JmqOMIRC+HqdGv9GGqC/0PB0Fv6LXi8GdzJQPMdoRLR0rvVykNeIzmcimejoIVjI78XUZeB1hF7p55h6W4C4Xm/PrnzKuXw4lBVehZtRhyIvNO62G/eNEZ3tup1/m+vkzHQIDAQAB";

    String encryptedMessageString = getUrlEncodedCipherText(messageString, serverPubKey, Base64.NO_WRAP);
    System.out.println("encryptedMessageString: " + encryptedMessageString);
    /**
     * This works! Ciphertext always comes out different, as expected, and decodes successfully when fed into Java application.
     */
}

public static String getUrlEncodedCipherText(String plainText, String pubKey, int base64Type){
    try {
        final PublicKey publicKey = loadPublicKey(pubKey, base64Type);
        final byte[] cipherBytes = encrypt(plainText, publicKey);
        String cipherText = base64Encode(cipherBytes, base64Type);
        String urlEncodedCipherText = urlEncode(cipherText);
        return urlEncodedCipherText;
    }
    catch (Exception e){
        e.printStackTrace();
        return null;
    }
}

public static final String ALGORITHM = "RSA";
public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

public static PublicKey loadPublicKey(String stored, int base64Type) throws GeneralSecurityException {
    String pubKey = stored.replace(BEGIN_PUBLIC_KEY, "");
    pubKey = pubKey.replace(END_PUBLIC_KEY, "");

    byte[] data = Base64.decode(pubKey, base64Type);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
    PublicKey pub = fact.generatePublic(spec);
    return pub;
}

public static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;
    try {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text.getBytes());
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return cipherText;
}

public static String base64Encode(byte[] cipherBytes, int base64Type){
    byte[] base64Cipher = Base64.encode(cipherBytes, base64Type);
    return new String(base64Cipher);
}

public static String urlEncode(String text){
    return text.replace("+", "-").replace("/", "_").replace("=", ",");
}
