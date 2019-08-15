          String text = "Test TEST";         
          String key = "deadbeefbeefdead"; // 128 bit key
         // Create key and cipher
         Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");
         // encrypt the text
         cipher.init(Cipher.ENCRYPT_MODE, aesKey);
         byte[] encrypted = cipher..doFinal(text.getBytes());
         System.err.println(new String(encrypted));
         // decrypt the text
         cipher.init(Cipher.DECRYPT_MODE, aesKey);
         String decrypted = new String(cipher.doFinal(encrypted));
         System.err.println(decrypted
