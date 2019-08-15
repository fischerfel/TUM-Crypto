public void createSecretKey(String password){
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    secret = new SecretKeySpec(tmp.getEncoded(), "AES");
}
