/**
 * Encrypt a object and write it to the given outstream
 * @param object the serializable object
 * @param ostream The outstream
 */
private void encrypt(Serializable object, OutputStream ostream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    try {
        SecretKeySpec sks = new SecretKeySpec(key, transformation);

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        SealedObject sealedObject = new SealedObject(object, cipher);

        CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
        @SuppressWarnings("resource")
        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
        outputStream.writeObject(sealedObject);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
}

/**
 * Decrypt a object from the given InputStream
 * @param istream the inputstream
 * @return the decrypted object
 */
private Object decrypt(InputStream istream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    SecretKeySpec sks = new SecretKeySpec(key, transformation);
    Cipher cipher = Cipher.getInstance(transformation);
    cipher.init(Cipher.DECRYPT_MODE, sks);

    CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
    @SuppressWarnings("resource")
    ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
    SealedObject sealedObject;
    try {
        sealedObject = (SealedObject) inputStream.readObject();
        return sealedObject.getObject(cipher);
    } catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
        return null;
    }

}
