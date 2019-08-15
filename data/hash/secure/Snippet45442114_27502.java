public static String encrypt() throws Exception{
    String myiv = "somevalue";
    String mykey = "somevalue";
    String mydata = "somevalue";
    String new_text = "";

    RijndaelEngine rijndael = new RijndaelEngine(256);
    CBCBlockCipher cbc_rijndael = new CBCBlockCipher(rijndael);
    ZeroBytePadding c = new ZeroBytePadding();
    PaddedBufferedBlockCipher pbbc = new PaddedBufferedBlockCipher(cbc_rijndael, c);

    byte[] iv_byte = sha256(myiv);

    byte[] givenKey = sha256(mykey);

    CipherParameters keyWithIV = new ParametersWithIV(new KeyParameter(givenKey), iv_byte);

    pbbc.init(true, keyWithIV);
    byte[] plaintext = mydata.getBytes(Charset.forName("UTF-8"));
    byte[] ciphertext = new byte[pbbc.getOutputSize(plaintext.length)];
    int offset = 0;
    offset += pbbc.processBytes(plaintext, 0, plaintext.length, ciphertext, offset);
    offset += pbbc.doFinal(ciphertext, offset);
    new_text = new String(new Base64().encode(ciphertext), Charset.forName("UTF-8"));
    System.out.println(new_text);
    return new_text;
}

public static byte[] sha256(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] messageDigest = md.digest(input.getBytes(Charset.forName("UTF-8")));
    return messageDigest;
}
