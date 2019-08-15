DESKeySpec dks = new DESKeySpec("keyword".getBytes()); 
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
SecretKey key = keyFactory.generateSecret(dks);

Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
SecureRandom sr = new SecureRandom();  
cipher.init( Cipher.DECRYPT_MODE, key ,sr); 

byte b[] = response.toByteArray();      
byte decryptedData[] = cipher.doFinal( b );
