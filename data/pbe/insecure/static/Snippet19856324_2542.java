Cipher dcipher;

byte[] salt = new String("12345678").getBytes();
int iterationCount = 1024;
int keyStrength = 256;
SecretKey key;
byte[] iv;

Decrypter(String passPhrase) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    System.out.println("factory +" + factory);
    KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt,
            iterationCount, keyStrength);
    System.out.println("spec  " + spec);
    SecretKey tmp = factory.generateSecret(spec);
    System.out.println();
    key = new SecretKeySpec(tmp.getEncoded(), "AES");
    dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
}

public String encrypt(String data) throws Exception {
    dcipher.init(Cipher.ENCRYPT_MODE, key);
    AlgorithmParameters params = dcipher.getParameters();
    iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] utf8EncryptedData = dcipher.doFinal(data.getBytes());
    String base64EncryptedData = new sun.misc.BASE64Encoder()
            .encodeBuffer(utf8EncryptedData);

    System.out.println("IV "
            + new sun.misc.BASE64Encoder().encodeBuffer(iv));
    System.out.println("Encrypted Data " + base64EncryptedData);
    return base64EncryptedData;
