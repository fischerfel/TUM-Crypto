       Cipher cipher = Cipher.getInstance("RSA");  
       cipher.init(Cipher.DECRYPT_MODE, myPrivKey);  
       byte[] descryptedData = cipher.doFinal(cyphertext.getBytes());  
