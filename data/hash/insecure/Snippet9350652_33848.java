public class MessageEncrypt {

public String encryptString(String message, String seckey) throws Exception{
    byte[] encData = encrypt(message, seckey);

    return this.getHexString(encData, "");
}

public String decryptString(String message, String seckey) throws Exception{
    return decrypt(this.getBArray(message), seckey);
}

private byte[] encrypt(String message, String seckey) throws Exception {
    final MessageDigest md = MessageDigest.getInstance("md5");
    final byte[] digestOfPassword = md.digest(seckey.getBytes("utf-8"));
    final byte[] keyBytes = acopyof(digestOfPassword, 24);
    for (int j = 0, k = 16; j < 8;) {
        keyBytes[k++] = keyBytes[j++];
    }

    final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    final byte[] plainTextBytes = message.getBytes("utf-8");
    final byte[] cipherText = cipher.doFinal(plainTextBytes);
    // final String encodedCipherText = new sun.misc.BASE64Encoder()
    // .encode(cipherText);

    return cipherText;
}

private String decrypt(byte[] message, String seckey) throws Exception {
    final MessageDigest md = MessageDigest.getInstance("md5");
    final byte[] digestOfPassword = md.digest(seckey.getBytes("utf-8"));
    final byte[] keyBytes = acopyof(digestOfPassword, 24);
    for (int j = 0, k = 16; j < 8;) {
        keyBytes[k++] = keyBytes[j++];
    }

    final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    decipher.init(Cipher.DECRYPT_MODE, key, iv);

    final byte[] plainText = decipher.doFinal(message);

    return new String(plainText, "UTF-8");
}

private String getHexString(byte[] barray, String delim) {
    StringBuffer buffer = new StringBuffer();


    for (int i = 0; i < barray.length; i++) {
        int ii = barray[i] & 0xFF;
        String bInt = Integer.toHexString(ii);
        if (ii < 16) {
            bInt = "0" + bInt.toUpperCase();
        }
        buffer.append(bInt);
        if (i < barray.length - 1) {
            buffer.append(delim);
        }
    }

    return buffer.toString().toUpperCase();
}

private byte[] getBArray(String bString) {
    byte[] retBytes;

    if (bString.length() % 2 != 0) {
        return new byte[0];
    }
    retBytes = new byte[bString.length() / 2];

    for (int i = 0; i < bString.length() / 2; i++) {
        retBytes[i] = (byte) ((Character.digit(bString.charAt(2 * i), 16) << 4) + Character.digit(bString.charAt(2 * i + 1), 16));
    }
    return retBytes;
}

public static byte[] acopyof(byte[] orig, int newlength){
    byte[] copya = new byte[newlength];
    for(int i=0;i< orig.length;i++){
        copya[i]=orig[i];
    }
    for(int i=orig.length;i<newlength;i++){
        copya[i]=0x0;
    }
    return copya;
}
