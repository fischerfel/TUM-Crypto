private static final int pswdIterations = 1000;
private static final int keySize = 256;
private static final int saltlength = keySize / 8;

private static final String ENCODING = "UTF-8";
private static final String PBK = "PBKDF2WithHmacSHA1";
private static final String AES = "AES";
private static final String CIPHER = "AES/CBC/PKCS5Padding";

public String encrypt(String plainText) throws Exception {
    //get text from password field
    final String pass = password.getText().toString();
    //get salt from generateSalt() method (see below)
    String salt = generateSalt();
    //convert salt to bytes
    byte[] saltBytes = salt.getBytes(ENCODING);

    // Derive the key from
    SecretKeyFactory factory = SecretKeyFactory.getInstance(PBK);
    PBEKeySpec spec = new PBEKeySpec(
            pass.toCharArray(),
            saltBytes,
            pswdIterations,
            keySize
    );

    //encode key
    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), AES);

    //encrypt the message
    Cipher cipher = Cipher.getInstance(CIPHER);
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes(ENCODING));

    //encode text and output final encrypted text
    String encodedText = Base64.encodeToString(encryptedTextBytes, Base64.DEFAULT);
    String encodedIV = Base64.encodeToString(ivBytes, Base64.DEFAULT);
    String encodedSalt = Base64.encodeToString(saltBytes, Base64.DEFAULT);
    return encodedSalt +  encodedText + encodedIV;
}

public static String generateSalt() {
    SecureRandom random = new SecureRandom();
    byte bytes[] = new byte[saltlength];
    random.nextBytes(bytes);
    return new String(bytes);
}
