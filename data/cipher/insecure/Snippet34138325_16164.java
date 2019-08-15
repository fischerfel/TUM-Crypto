  private static final byte[] __RawKey = {
   (byte) 0x30, (byte) 0x31, (byte) 0x32,
   (byte) 0x33, (byte) 0x34, (byte) 0x35,
   (byte) 0x36, (byte) 0x37
    };

   private String decrypt(String data) throws Exception {
   try {
     Key key = new SecretKeySpec(__RawKey, 0, __RawKey.length, "DES");
     byte[] _encrypted = data.getBytes();
     String sKey = new String(__RawKey);
     System.out.println(sKey);
     System.out.println(sKey.length());

     Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding", "SunJCE");                 
     cipher.init(Cipher.DECRYPT_MODE, key);
     byte[] _decrypted = cipher.doFinal(_encrypted);
     System.out.println("Decrypted: " + new String(_decrypted));
     return new String(_decrypted);
     }
     catch (Exception e) {
     System.out.println(e);
     return null;
     }
     }  
