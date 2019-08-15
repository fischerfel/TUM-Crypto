  private PrivateKey privateKey = null;
private PublicKey publicKey = null;

public RSA() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
    final PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(RSA.byteArrPrivate));
    final PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(RSA.byteArrPublic));
    this.privateKey = privateKey;
    this.publicKey = publicKey;

}

public PrivateKey getPrivateKey() {
    return privateKey;
}

public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
}

public PublicKey getPublicKey() {
    return publicKey;
}

public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
}

public String byteToString(byte[] encrypted) {
    char[] cbuf = new char[encrypted.length];
    for (int i = 0; i < encrypted.length; i++) {
        cbuf[i] = (char) encrypted[i];
    }
    String encryptedString = new String(cbuf);
    return encryptedString;
}

public byte[] stringToByte(String encryptedString) {
    byte[] out = new byte[encryptedString.length()];
    for (int i = 0; i < encryptedString.length(); i++) {
        out[i] = (byte) encryptedString.charAt(i);
    }
    return out;
}

public byte[] encrypt(String message, PublicKey pk) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pk);
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    byte[] chiffrat = null;
    try {
        chiffrat = cipher.doFinal(message.getBytes());
    } catch (IllegalBlockSizeException | BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return chiffrat;
}

public String decrypt(byte[] chiffrat, PrivateKey sk) {
    byte[] dec = null;
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, sk);
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (NoSuchPaddingException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try {
        dec = cipher.doFinal(chiffrat);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return new String(dec);
}
