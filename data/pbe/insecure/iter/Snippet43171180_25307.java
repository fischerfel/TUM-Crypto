 public static Key keyGen(String k) throws Exception {
     SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
     KeySpec spec = new PBEKeySpec(k.toCharArray(), k.getBytes(), 12, 128);
     SecretKey tmp = factory.generateSecret(spec);
     return new SecretKeySpec(tmp.getEncoded(), "AES");
 }
