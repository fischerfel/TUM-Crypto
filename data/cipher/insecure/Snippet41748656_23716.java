public void decode() {
    byte[] KeyData = {0x2f, 0x18, (byte) 0xf5, (byte) 0x96, (byte) 0xa2, 0x17, 0x18, 0x29};
    byte[] IV = {0x01, 0x3d, (byte) 0xcf, (byte) 0xe2, (byte) 0xe3, (byte) 0xf9, (byte) 0xdd, (byte) 0x81};
    SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("Blowfish/CFB/NoPadding");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    }

    byte[] ciphertext = hexToBytes("424749c1d3b497");

    // Decrypt
    try {
        cipher.init(Cipher.DECRYPT_MODE, KS, new javax.crypto.spec.IvParameterSpec(IV));
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    byte[] message = new byte[0];
    try {
        message = cipher.doFinal(ciphertext);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    System.out.println("-- Decrypted -----------");
    System.out.println("HEX:\t " + bytesToHex(message));
    System.out.println("PLAIN:\t " + new String(message));

}

public byte[] hexToBytes(String str) {
    if (str == null) {
        return null;
    } else if (str.length() < 2) {
        return null;
    } else {
        int len = str.length() / 2;
        byte[] buffer = new byte[len];
        for (int i = 0; i < len; i++) {
            buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
        }
        return buffer;
    }

}

public  String bytesToHex(byte[] bytes) {

    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
