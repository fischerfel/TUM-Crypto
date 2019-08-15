 byte[] keyBytes = "vikoAmrPass12345".getBytes(); 
 SecretKeySpec key = new SecretKeySpec(keyBytes, "AES"); 
 Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding"); 
 cipher.init(Cipher.DECRYPT_MODE, key); 
 byte [] out = cipher.doFinal(inv); 
 System.out.println("Decrypted: " +new String(out));
