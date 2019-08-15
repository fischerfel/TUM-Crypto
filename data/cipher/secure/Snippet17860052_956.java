String encoded = null;
byte[] encrypted = null;
String plaintext = "...";

try {
    String privKeyPEM = "...";
    byte[] decoded = Base64.decode(privKeyPEM, Base64.DEFAULT);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey privKey = kf.generatePrivate(spec);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, privKey);

    encrypted = cipher.doFinal(plaintext.getBytes());
    encoded = Base64.encodeToString(encrypted, Base64.DEFAULT);
}
catch (Exception e) {
    e.printStackTrace();
}
