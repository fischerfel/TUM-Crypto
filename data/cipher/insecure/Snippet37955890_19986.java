   SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(),
    "AES");
   Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
   cipher.init(1, secretKeySpec);

   byte[] aBytes = cipher.doFinal(inputString.getBytes());
