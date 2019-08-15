          SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF-8"), "Blowfish");
         Cipher cipher = Cipher.getInstance("Blowfish");
         cipher.init(Cipher.DECRYPT_MODE, key);
         byte[] decrypted = cipher.doFinal(encryptedData);
         return new String(decrypted);
