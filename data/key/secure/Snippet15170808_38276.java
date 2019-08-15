 public void initKey(String key) {
    String paddedKey = Utils.padString(key);
    mKeyspec = new SecretKeySpec(Utils.getBytes(paddedKey), "AES/ECB/NoPadding");
                   // Utils.getBytes returns "paddedKey.getBytes("CP1252")"
 }

public byte[] encrypt2(String data) {
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, mKeyspec);
        String paddedData = Utils.padString(data);
        return cipher.doFinal(Utils.getBytes(paddedData));

    } catch(InvalidKeyException e) {
        e.printStackTrace();
    // Series of catch blocks
    }
    return null;
}

public String decrypt2(byte[] cypherText) {
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, mKeyspec);
        byte[] plainTextBytes = cipher.doFinal(cypherText);
        return Utils.getString(plainTextBytes);
        // Utils.getString returns "new String(bytes, "CP1252");"
    } catch(InvalidKeyException e) {
        // Series of catch blocks.
    } 
    return null;
}
