public Cryptography(char[] password) throws NoSuchAlgorithmException,
        InvalidKeySpecException, NoSuchPaddingException {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
    KeySpec spec = new PBEKeySpec(password, salt, 1024, 256);
    secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    cipher = Cipher.getInstance(AES/CBC/PKCS5Padding);
}
