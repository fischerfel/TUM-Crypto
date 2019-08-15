public void decrypt(String encryptedText) {
    byte[] decryptedData = null;  
    try { 
      FileInputStream is = new FileInputStream("/Users/rajat/workspace/dev/abc.jks");    
      KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      keystore.load(is, "abc".toCharArray());
      String alias = "xyz";
      Key key = keystore.getKey(alias, "abc".toCharArray());

      Cipher cipher = Cipher.getInstance("RSA");  
      cipher.init(Cipher.DECRYPT_MODE, key);  
      decryptedData = cipher.doFinal(encryptedText.getBytes());  
      System.out.println("Decrypted Data: " + new String(decryptedData));  
    } catch (Exception e) {  
     e.printStackTrace();  
    }   
  }
