SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithHmacSHA512AndAES_128");
KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT.getBytes(), 4096, 128);
SecretKey tmp = factory.generateSecret(spec);
SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(),"PBEWithHmacSHA512AndAES_128" ); 
Cipher cipher = Cipher.getInstance("PBEWITHHMACSHA512ANDAES_128"); 
cipher.init(Cipher.ENCRYPT_MODE, secret);
