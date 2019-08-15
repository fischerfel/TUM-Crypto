private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}

public void decryptDatabase(String k, String iv) throws InvalidKeyException, InvalidAlgorithmParameterException,
        NoSuchAlgorithmException, NoSuchPaddingException, IOException {

    File extStore = Environment.getExternalStorageDirectory();
    FileInputStream fis = new FileInputStream(extStore
            + "/WhatsApp/Databases/msgstore.db.crypt7.nohdr");
    FileOutputStream fos = new FileOutputStream(extStore + "/Decrypted.db");

    SecretKeySpec sks = new SecretKeySpec(hexStringToByteArray(k), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, sks,
            new IvParameterSpec(hexStringToByteArray(iv)));
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();
}
