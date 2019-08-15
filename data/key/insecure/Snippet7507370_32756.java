Cipher cipher = Cipher.getInstance("AES"); 
SecretKey key = new SecretKeySpec(new byte[64], "AES"); // 256 bit key for AES      
cipher.init(Cipher.ENCRYPT_MODE, key); 
