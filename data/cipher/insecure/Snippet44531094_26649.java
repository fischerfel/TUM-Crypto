public void aliceEncrypt(byte[] plaintext, byte[] sharedSecret, Socket socket) {

    try {
        Cipher cipher = Cipher.getInstance("RC4/ECB/NoPadding");
        Key sk = new SecretKeySpec(sharedSecret, "RC4");
        cipher.init(Cipher.ENCRYPT_MODE, sk);
        CipherOutputStream cos = new CipherOutputStream(socket.getOutputStream(), cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject(plaintext);
        oos.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}


public byte[] bobDecrypt( byte[] sharedSecret, Socket socket) {


    try {
        Cipher cipher = Cipher.getInstance("RC4/ECB/NoPadding");
        Key sk = new SecretKeySpec(sharedSecret, "RC4");
        cipher.init(Cipher.DECRYPT_MODE, sk);
        CipherInputStream cis = new CipherInputStream(socket.getInputStream(), cipher);
        ObjectInputStream ois = new ObjectInputStream(cis);
        byte[] bytes = (byte[]) ois.readObject();
        return bytes;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
