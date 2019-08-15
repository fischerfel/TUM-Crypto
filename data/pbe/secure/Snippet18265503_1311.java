public static SecretKey getExistingKey(String password, byte[] salt) throws Exception{
    SecretKey key= null;
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    byte[] keyBytes=new byte[32]; 
    keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
    key= new SecretKeySpec(keyBytes, "AES");

    return key;
}
