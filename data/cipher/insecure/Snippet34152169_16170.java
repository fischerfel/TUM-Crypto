private void switchToChipherStreams(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
    //Generate key
    byte[] key = new byte[8];
    for (int i = 0; i < 8; i++) {
        if (password.length() > i) {
            key[i] = password.getBytes()[i];
        } else {
            key[i] = (byte) i;
        }
    }
    //Setup cipher streams
    SecretKey key64 = new SecretKeySpec(key, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, key64);
    in = new ObjectInputStream(new CipherInputStream(socket.getInputStream(), cipher));
    out = new ObjectOutputStream(new CipherOutputStream(socket.getOutputStream(), cipher));
    out.reset();
    out.flush();
    out.writeObject("switch");
}
