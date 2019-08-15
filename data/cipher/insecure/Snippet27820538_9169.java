 key = new SecretKeySpec(keyValue, "AES");
 try {
     cipher = Cipher.getInstance("AES");
 } catch (Exception e) {
     e.printStackTrace();
 }
