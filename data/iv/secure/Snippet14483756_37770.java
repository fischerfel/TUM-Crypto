public static void main(String[] args) throws Exception {
     // generate key
     KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
     SecretKey secretKey = keyGen.generateKey();
     // get Cipher and init it for encryption
     Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
     cipher.init(Cipher.ENCRYPT_MODE, secretKey);
     String data="u7mzqw2";

     // encrypt data
     byte[] cipherText = cipher.doFinal(data.getBytes());
     // get the initialization vector from the cipher
     byte[] ivBytes = cipher.getIV();
     IvParameterSpec iv = new IvParameterSpec(ivBytes);

     byte[] keyBytes = secretKey.getEncoded();
     // create a SecretKeySpec from key material
     SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "Blowfish");
     // get Cipher and init it for encryption
     cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
     cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
     byte[] plainText = cipher.doFinal(cipherText);
     System.out.println(new String(plainText));
}
