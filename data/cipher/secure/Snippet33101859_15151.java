// add proper exception handling, left out only for the example
public static void main(String[] args) throws Exception {
    String transformation = "AES/CBC/PKCS5PADDING";
    String provider = "SunJCE";
    String algorithm = "AES";
    byte[] payloadArray = "secret text".getBytes();

    KeyGenerator keyGen = KeyGenerator.getInstance(algorithm, provider);
    keyGen.init(128);
    Key key = keyGen.generateKey();

    byte[] ivBytes = new byte[16];
    SecureRandom prng = new SecureRandom();
    prng.nextBytes(ivBytes);
    IvParameterSpec IV = new IvParameterSpec(ivBytes);

    Cipher encryptCipher = Cipher.getInstance(transformation, provider);
    encryptCipher.init(Cipher.ENCRYPT_MODE, key, IV);

    InputStream payload = new ByteArrayInputStream(payloadArray);
    InputStream encryptStream = new CipherInputStream(payload, encryptCipher);

    Cipher decryptCipher = Cipher.getInstance(transformation, provider);
    decryptCipher.init(Cipher.DECRYPT_MODE, key, IV);
    InputStream decryptStream = new CipherInputStream(encryptStream, decryptCipher);

    for (int i = decryptStream.read(); i >= 0; i = decryptStream.read()) {
        System.out.print((char) i);
    }
}
