private String decryptAuthorizationString(String authString, String password) {
  try {
    //Force the test string
    authString = "c1W2YO1vYQzu6czteEidrG0U4g5gT4h57vAlP7tdjcY=";
    //Force the test password
    password = "GAT";

    //Create the password and initial vector bytes
    byte[] passwordBytes= new byte[32];      
    byte[] b= password.getBytes("UTF-8");      
    int len= b.length;
    if (len > passwordBytes.length) len = passwordBytes.length;
      System.arraycopy(b, 0, passwordBytes, 0, len);

    byte[] ivBytes= new byte[16];
    System.arraycopy(passwordBytes, 0, ivBytes, 0, 16);

    //Get the authString as byte[]
    byte[] authBytes = new BASE64Decoder().decodeBuffer(authString);

    InputStream inputStream = new ByteArrayInputStream(authBytes);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // If you have Bouncycastle library installed, you can use
    // Rijndael/CBC/PKCS7PADDING directly.
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS7PADDING", "BC");

    // convertedSecureString and initVector must be byte[] with correct length
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(passwordBytes, "AES"), new IvParameterSpec(ivBytes));

    CipherInputStream cryptoStream = new CipherInputStream(inputStream, cipher);
    byte[] buffer = new byte[1024];
    len = cryptoStream.read(buffer, 0, buffer.length);
    while (len > 0) {
      outputStream.write(buffer, 0, len);
      len = cryptoStream.read(buffer, 0, buffer.length);
    }

    outputStream.flush();
    cryptoStream.close();
    String resStr = outputStream.toString("UTF-8");       
    return resStr; //<<--- resStr must be "101714994"
  } catch (Throwable t) {

  }
  return null;
}
