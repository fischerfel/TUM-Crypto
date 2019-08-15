Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

ECGenParameterSpec brainpoolP160R1 = new ECGenParameterSpec("brainpoolP256t1");
KeyPairGenerator kpg = null;
try {
    kpg = (KeyPairGenerator) KeyPairGenerator.getInstance("ECIES", "SC");
} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
    e.printStackTrace();
}

try {
    kpg.initialize(brainpoolP160R1, new SecureRandom());
} catch (InvalidAlgorithmParameterException e) {
    e.printStackTrace();
}

KeyPair akey = kpg.generateKeyPair();
KeyPair bkey = kpg.generateKeyPair();
// PublicKey publicKey = keyPair.getPublic();
//PrivateKey privateKey = keyPair.getPrivate();

byte[] d = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
byte[] e = new byte[]{8, 7, 6, 5, 4, 3, 2, 1};

IESParameterSpec param = new IESParameterSpec(d, e, 256);

Cipher c = null;

try {
    c = Cipher.getInstance("ECIES");
} catch (NoSuchAlgorithmException | NoSuchPaddingException f) {
    f.printStackTrace();
}

try {
    c.init(Cipher.ENCRYPT_MODE, new IEKeySpec(akey.getPrivate(), bkey.getPublic()), param);
    //c.init(Cipher.ENCRYPT_MODE, c1Key, param);
    //c.init(Cipher.ENCRYPT_MODE, publicKey, new SecureRandom());
    // How can i put the AES128_CBC for ies parameter ? is that possible
} catch (InvalidKeyException | InvalidAlgorithmParameterException f) {
    f.printStackTrace();
}
byte[] message = theTestText.getBytes();
byte[] cipher = new byte[0];
try {
    cipher = c.doFinal(message);//,0,message.length);
} catch (IllegalBlockSizeException | BadPaddingException f) {
    f.printStackTrace();
}

TextView eccencoded = (TextView) findViewById(R.id.eccencoded);
eccencoded.setText("[ENCODED]:\n" +
        new String(cipher) + "\n");

try {
    c.init(Cipher.DECRYPT_MODE, new IEKeySpec(bkey.getPrivate(), akey.getPublic()), param);
} catch (InvalidKeyException | InvalidAlgorithmParameterException f) {
    f.printStackTrace();
}

byte[] plaintext = new byte[0];
try {
    plaintext = c.doFinal(cipher);
} catch (IllegalBlockSizeException | BadPaddingException f) {
    f.printStackTrace();
}
TextView eccdecoded = (TextView) findViewById(R.id.eccdecoded);
eccdecoded.setText("[DECODED]:\n" +
        new String(plaintext) + "\n");
