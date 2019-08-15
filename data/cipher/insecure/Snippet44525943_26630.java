    public void aliceEncrypt(byte[] plaintext, byte[] sharedSecret) {

    Cipher cipher;
    byte[] encrypted = null;
    try {
        cipher = Cipher.getInstance("RC4");
        Key sk = new SecretKeySpec(sharedSecret, "RC4");
        cipher.init(Cipher.ENCRYPT_MODE, sk);
        encrypted = cipher.doFinal(plaintext);
        CipherOutputStream cos = new CipherOutputStream(socket.getOutputStream(), cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject(encrypted);
        oos.flush();

    } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
}
