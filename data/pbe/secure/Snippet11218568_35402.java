public String encryptToString(Serializable object) {
    SecretKeyFactory keyFactory =
            SecretKeyFactory.getInstance(ALGORITHM);
    KeySpec keySpec = new PBEKeySpec(password.toCharArray());
    SecretKey secretKey = keyFactory.generateSecret(keySpec);
    PBEParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATIONS);

    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
    // Serialize map
    final ByteArrayOutputStream byteArrayOutputStream =
            new ByteArrayOutputStream();
    CipherOutputStream cout =
            new CipherOutputStream(byteArrayOutputStream, cipher);
    ObjectOutputStream out = new ObjectOutputStream(cout);
    out.writeObject(object);
    out.close();
    cout.close();
    byteArrayOutputStream.close();
    return new String(
            Base64.encode(byteArrayOutputStream.toByteArray()));
}
