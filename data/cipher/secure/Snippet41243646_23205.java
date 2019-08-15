private void exampleMethod(){
    String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjS7T3WJ+VLucnUP5WYryeg+hhOjZZl5VxwvJAgo4GrXaXdernTPtmXnOSUlbhd928QRCip7D3rLwJNvGIwhPa6coA+YQnj+aHQC02AvCJP/9jpeNmm5MASZfYFXrdmOrMhAPpDZ4rUk1mqtvpwBkYmW3VbMtG336wT1bAIKPHCZuI2n6glupJvs8gK0NuIoAPRlxiQmQD7NCcRx1Et4JmqOMIRC+HqdGv9GGqC/0PB0Fv6LXi8GdzJQPMdoRLR0rvVykNeIzmcimejoIVjI78XUZeB1hF7p55h6W4C4Xm/PrnzKuXw4lBVehZtRhyIvNO62G/eNEZ3tup1/m+vkzHQIDAQAB";
    String privKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNLtPdYn5Uu5ydQ/lZivJ6D6GE6NlmXlXHC8kCCjgatdpd16udM+2Zec5JSVuF33bxBEKKnsPesvAk28YjCE9rpygD5hCeP5odALTYC8Ik//2Ol42abkwBJl9gVet2Y6syEA+kNnitSTWaq2+nAGRiZbdVsy0bffrBPVsAgo8cJm4jafqCW6km+zyArQ24igA9GXGJCZAPs0JxHHUS3gmao4whEL4ep0a/0YaoL/Q8HQW/oteLwZ3MlA8x2hEtHSu9XKQ14jOZyKZ6OghWMjvxdRl4HWEXunnmHpbgLheb8+ufMq5fDiUFV6Fm1GHIi807rYb940Rne26nX+b6+TMdAgMBAAECggEAOHrlUwmWFHvBmcCZvlKx0lbyfONSJXvTwP9b+K7x5u2dYDFpfEhL4zwxZGwuaw4M3TqhDCeboDnhjD1HtLgcXarPfU/KkiRLmRKxRkTd9ENcwnCqu38odMVPHpEA06nn0O1P9Je+C0TgZvHyhtLHVf3vLB+0Ce2KJUhQYZHZgp7dBt0jgsGlEvQTtc1q3UtElNYbcPdLhg16Dcvw93+C+CWizUxHKl2VAxYBz5fGBxa+Cc6xbj4OGd0mvK9Z+71cX8sgp+WIdSV3a2SZrwYq8XxdtJJfdOwPc6LE56Ul9y3fE/biG8DM8ysu7g4C2K1tjKBTq1Rzz4NBCaGwkWQwGQKBgQDCX5nOiHYi0OYoE3WEVYVuEaf/YljurMiU4xYsy3bXzI42S+iKfVHz+/tvTrXSE0n6AdJnDHyP7mncFZodaurYR90S9VW3P+aOGoysmOmBRWW30xyIZ1g5+PJ9xPzoBfyc/AGHO5w5roY7Gg5myUC+UbucFU+9/JHUVoGV8yFz+wKBgQC58f8iY37sXf/sguv0YHpUV1suPWT90kxC25eRQz0lHWIIw4gfGXI7Tf2vtx7hUPK1z3X9lPiTsqmZIRKPrvykD8djmpfZEOzZlLxxhHUZMZppx19xSsgHaespCZir68aytBWn7FOVpehJP7MWaJurg03V8Cv0NSPU5JhN/j8xxwKBgQCkb6I78oAWtilvz/6EJKGL244HZJkd2bibFH4HCV4R9HB/CLrCpoB1a0BsCueQwFa+FVp9aTfbv/N4iCHoLzJcJHeneTu5mmqe32ERBw/MF/yUhhnGX79o0+25brQSSjZKTHuyf0CMH9RZHZL/a9TE7XpM8k6SyKBKRaC9TYGIjwKBgDZG10x4coIYZi3pgWqSBuM7pJf4v9P/YNdbNgDm+aAt1YHtYXyCdv+solJ6R39Jm69KYYylwXGLg0n5h2t9jq1tFayTYaOw9xIEAoW4Pl4eRo597fQp+f2AA261KGV2q0danb+okjVqekV3XJU778S+zSeXtZzRLiZkm7iYcGXdAoGAUEbRAsopG+blDBmuC5Fhr7Grof9lw/zRgfl516n9oujOmDY8Sl4F7jHkgKzOL1CpvMbRswr6G3tmpJMYGNy0HVX4n4QAiIvEraLUtLlh7LotUiU8zzjniK7HPANoOsQxEi6iUR6jWRf0LKgckE9aaUJjvljj1Hr4PbFpny2gjD4=";

    String message = "This actually works in Java AND Android!";
    String encryptedMessage = getUrlEncodedCipherText(message, pubKey);

    try {
        byte[] base64Decoded = Base64.decodeBase64(encryptedMessage.getBytes(Charset.forName("UTF-8")));
        String decryptedMessage = decrypt(base64Decoded, loadPrivateKey(privKey));
        System.out.println("decryptedMessage: " + decryptedMessage);
        /**
         * This works! Ciphertext always comes out different, as expected, and decodes successfully.
         */
    }
    catch (Exception e){
        e.printStackTrace();
    }
}

public static String getUrlEncodedCipherText(String plainText, String pubKey){
    try {
        final PublicKey publicKey = loadPublicKey(pubKey);
        final byte[] cipherBytes = encrypt(plainText, publicKey);
        String cipherText = base64Encode(cipherBytes);
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

public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
    String pubKey = stored.replace(BEGIN_PUBLIC_KEY, "");
    pubKey = pubKey.replace(END_PUBLIC_KEY, "");

    byte[] data = Base64.decodeBase64(pubKey);
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

public static String decrypt(byte[] encrypted, PrivateKey key) {
    byte[] decryptedText = null;
    try {
        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedText = cipher.doFinal(encrypted);
    }
    catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    return new String(decryptedText);
}

public static String base64Encode(byte[] cipherBytes){
    byte[] base64Cipher = Base64.encodeBase64(cipherBytes);
    return new String(base64Cipher);
}

public static String urlEncode(String text){
    return text.replace("+", "-").replace("/", "_").replace("=", ",");
}
