public class KripAsim {
String hasil;

public String encrypt(String text) {
    try {

        String PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvCNqSslgCndo8vfNrkXFDLXmst024Oi8D7LEiJFjYBva4kBKKISe8rKp58kCLLHjv90RN+Dy2KWcf0eFkKaqc3zILBI99JhV1z8TFOzmt5dfgW6fD1ucBfsK6pWxK84DddyOqKldwHlReqjuDHT2jLue51vpXaCa12WV5bMnGsfy3vZKnp699YCguqRpTR1MijZ9pz8WqldrR0a/DCaq5YxZ7lvjwuWIodQy3S3XRHAaeaUrFHFFLumzXAGuP447oRYR0p+1qsy8+wOtrsGm8m8bMg+C1XGMblkODtOFHz3wtrRZ5OwzgEm7J7odmSX8mSYBZYLcnUVqIFRsQkZLiwIDAQAB";

        byte [] decoded = Base64.decode(PUBLIC_KEY,Base64.NO_WRAP);
        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        PublicKey pubkey = keyFac.generatePublic(keySpec);

        Cipher rsa;
        rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.ENCRYPT_MODE, pubkey);
        hasil = Base64.encodeToString(rsa.doFinal(text.getBytes("UTF-8")),Base64.NO_WRAP);
        return hasil;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return hasil;

}
}
