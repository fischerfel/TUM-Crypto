 static public String encrypt(String message , String key , int keyLength) throws Exception {
     // Get the KeyGenerator
   KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(keyLength); // 192 and 256 bits may not be available
    // Generate the secret key specs.
     SecretKey skey = key; //here is the error
   byte[] raw = skey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    // Instantiate the cipher
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    System.out.println("msg is" + message + "\n raw is" + raw);
    byte[] encrypted = cipher.doFinal(message.getBytes());
    String cryptedValue = new String(encrypted);
    System.out.println("encrypted string: " + cryptedValue);
    return cryptedValue;
}
