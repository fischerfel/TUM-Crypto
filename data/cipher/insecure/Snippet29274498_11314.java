     Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

     Cipher cipher = Cipher.getInstance("AES");



     // encrypt the text

     cipher.init(Cipher.ENCRYPT_MODE, aesKey);

     byte[] encrypted = cipher.doFinal(text.getBytes());

     System.err.println(new String(encrypted)); 
