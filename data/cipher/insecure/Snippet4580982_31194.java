 public String decrypt(String message, SecretKey skey) {

    byte[] raw = skey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    // Instantiate the cipher
    Cipher cipher;

    byte[] original = null;
    try {
        cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        System.out.println("Original string: "
                + message);
        original = cipher.doFinal(message.trim().getBytes());  //here where I got the exception
        String originalString = new String(original);
       }
 //catches
