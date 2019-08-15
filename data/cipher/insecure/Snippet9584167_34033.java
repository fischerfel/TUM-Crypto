public Encrypt(AOBCore instance){
    try {
        String message="This is just an example";

           // Get the KeyGenerator

           KeyGenerator kgen = KeyGenerator.getInstance("AES");
           kgen.init(256); // 192 and 256 bits may not be available


           // Generate the secret key specs.
           SecretKey skey = kgen.generateKey(); //Cantget 'test' in here...
           byte[] raw = skey.getEncoded();

           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");


           // Instantiate the cipher

           Cipher cipher = Cipher.getInstance("AES");

           cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

           byte[] encrypted =
             cipher.doFinal(message.getBytes());
           System.out.println("encrypted string: " + asHex(encrypted));

           cipher.init(Cipher.DECRYPT_MODE, skeySpec);
           byte[] original =
             cipher.doFinal(encrypted);
           String originalString = new String(original);
           System.out.println("Original string: " +
             originalString + " " + asHex(original));
    } catch (Exception e) {
        instance.logMessage(e.getMessage());
    }
}

public static String asHex (byte buf[]) {
      StringBuffer strbuf = new StringBuffer(buf.length * 2);
      int i;

      for (i = 0; i < buf.length; i++) {
       if (((int) buf[i] & 0xff) < 0x10)
        strbuf.append("0");

       strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
      }

      return strbuf.toString();
     }
