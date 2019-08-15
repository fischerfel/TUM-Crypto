   KeyGenerator kgen = KeyGenerator.getInstance("AES");
   kgen.init(128); // 192 and 256 bits may not be available

   // Generate the secret key specs.
   SecretKey skey = kgen.generateKey();
   byte[] raw = skey.getEncoded();

   SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
