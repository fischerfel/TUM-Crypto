 public static String deskripsi(String chiperText, String key) {
   try {
     SecretKeySpec KS = new SecretKeySpec(key.getBytes(), "Blowfish");
     Cipher cipher = Cipher.getInstance("Blowfish");
     cipher.init(Cipher.DECRYPT_MODE, KS);
     byte[] decrypted = cipher.doFinal(Base64.decode(chiperText, Base64.NO_PADDING));
     return new String(decrypted);
   } catch (Exception e) {
     return "ERROR";
   }
 }
