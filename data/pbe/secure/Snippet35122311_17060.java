/* generate secretkey */
PBEKeySpec keySpec=new PBEKeySpec(username.toCharArray(),salt.getBytes(),1000,256);
SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
SecretKey tmp=keyFactory.generateSecret(keySpec);

/*transform key to keySpec*/
SecretKey key=new SecretKeySpec(tmp.getEncoded(),"AES");
