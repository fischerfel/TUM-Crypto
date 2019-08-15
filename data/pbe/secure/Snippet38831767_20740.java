static byte[] salt = new byte[8];
static {
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
}

public final void test() throws Exception {
    Message message = buildTestMessage(...);

    SecretKey secret = encode(message, new FileOutputStream("test.encrypted"), "01234567".toCharArray());
    Message message2 = decode(new FileInputStream("test.encrypted"), "01234567".toCharArray());

    // out sth. like from@domain <--> null
    System.out.println(message.getFrom()[0] + " <--> " + message2.getFrom());

}

private Message decode(
        InputStream mailFileInputStream, char[] password) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, MessagingException, IOException, InvalidKeySpecException {
    /* Derive the key, given password and salt. */
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "PBEWithHmacSHA256AndAES_128");

    Cipher cipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
    cipher.init(Cipher.DECRYPT_MODE, secret);       

    InputStream is = new CipherInputStream(mailFileInputStream, cipher);
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    Message message = new MimeMessage(session, is );
    return message;
}

static SecretKey encode(
        Message message,
        FileOutputStream out,
        char[] password) throws Exception {

    /* Derive the key, given password and salt. */
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "PBEWithHmacSHA256AndAES_128");

    Cipher cipher  = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
    cipher.init(Cipher.ENCRYPT_MODE, secret);

    OutputStream cos = new CipherOutputStream(out, cipher);
    message.writeTo(cos);
    cos.close();

    return secret;
}
