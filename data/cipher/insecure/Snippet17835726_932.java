private static final String KEY = "57238004e784498bbc2f8bf984565090";

public static String encrypt(final String plaintext) throws GeneralSecurityException {
    SecretKeySpec sks = new SecretKeySpec(hexStringToByteArray(KEY), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
    byte[] encrypted = cipher.doFinal(plaintext.getBytes());
    return byteArrayToHexString(encrypted);
}

public static byte[] hexStringToByteArray(String s) {
    byte[] b = new byte[s.length() / 2];
    for (int i = 0; i < b.length; i++) {
        int index = i * 2;
        int v = Integer.parseInt(s.substring(index, index + 2), 16);
        b[i] = (byte) v;
    }
    return b;
}

public static String byteArrayToHexString(byte[] b) {
    StringBuilder sb = new StringBuilder(b.length * 2);
    for (int i = 0; i < b.length; i++) {
        int v = b[i] & 0xff;
        if (v < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(v));
    }
    return sb.toString().toUpperCase();
}
