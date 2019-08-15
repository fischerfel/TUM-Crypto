public byte[] bobDecrypt( byte[] sharedSecret) {


    Cipher cipher = null;
    byte[] bytes = null;
    byte[] decrypted = null;
    try {
        cipher = Cipher.getInstance("RC4");
        Key sk = new SecretKeySpec(sharedSecret, "RC4");
        cipher.init(Cipher.DECRYPT_MODE, sk);
        CipherInputStream cis = new CipherInputStream(socket.getInputStream(), cipher);
        ObjectInputStream ois = new ObjectInputStream(cis);
        bytes =  (byte[])ois.readObject();
        decrypted = cipher.doFinal(bytes);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException | ClassNotFoundException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    return decrypted;
}
