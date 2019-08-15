private void switchToChipherStreams(String username) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
    byte key[] = dbMediator.getPasswordCypher(username);
    SecretKey key64 = new SecretKeySpec(key, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, key64);
    try {
        Thread.sleep(1000);
    } catch (InterruptedException ex) {
        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
    out = new ObjectOutputStream(new CipherOutputStream(socket.getOutputStream(), cipher));
    out.reset();
    out.flush();
    out.writeObject("switch");
    in = new ObjectInputStream(new CipherInputStream(socket.getInputStream(), cipher));
}
