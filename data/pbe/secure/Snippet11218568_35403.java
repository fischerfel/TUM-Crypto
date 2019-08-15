public Object decryptToObject(String encodedString) {
    SecretKeyFactory keyFactory =
            SecretKeyFactory.getInstance(ALGORITHM);
    KeySpec keySpec = new PBEKeySpec(password.toCharArray());
    SecretKey secretKey = keyFactory.generateSecret(keySpec);
    PBEParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATIONS);
    Cipher decipher = Cipher.getInstance(ALGORITHM);
    decipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
    final ByteArrayInputStream byteArrayInputStream =
            new ByteArrayInputStream(Base64.decode(encodedString
                    .getBytes()));
    CipherInputStream cin =
            new CipherInputStream(byteArrayInputStream, decipher);
    ObjectInputStream in = new ObjectInputStream(cin);
    Object result = in.readObject();
    in.close();
    cin.close();
    byteArrayInputStream.close();
    return result;
}
