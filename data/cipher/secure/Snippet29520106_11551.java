Cipher c = null;

try {
    c =Cipher.getInstance("ECIES", "SC"); //Cipher.getInstance("ECIESwithAES/DHAES/PKCS7Padding", "BC");
} catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
    e.printStackTrace();
}

try {
    c.init(Cipher.ENCRYPT_MODE,(IESKey)publicKey , new SecureRandom()); 
} catch (InvalidKeyException e) {
    e.printStackTrace();
}
byte[] message = "hello world -- a nice day today".getBytes();
byte[] cipher = new byte[0];
try {
    cipher = c.doFinal(message,0,message.length);
} catch (IllegalBlockSizeException | BadPaddingException e) {
    e.printStackTrace();
}
// System.out.println("Ciphertext : "+ Base64.encode(cipher));
TextView eccencoded = (TextView) findViewById(R.id.eccencoded);
eccencoded.setText("[ENCODED]:\n" +
Base64.encodeToString(cipher, Base64.DEFAULT) + "\n");

try {
    c.init(Cipher.DECRYPT_MODE,(IESKey) privateKey, new SecureRandom());
} catch (InvalidKeyException e) {
    e.printStackTrace();
}

byte[] plaintext = new byte[0];
try {
    plaintext = c.doFinal(cipher,0,cipher.length);
} catch (IllegalBlockSizeException | BadPaddingException e) {
    e.printStackTrace();
}
TextView eccdecoded = (TextView) findViewById(R.id.eccdecoded);
eccdecoded.setText("[DECODED]:\n" +
                Base64.encodeToString(plaintext, Base64.DEFAULT) + "\n");
