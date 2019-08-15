public static String generateHash256Value(String msg, String key) {
    MessageDigest m = null;
    String hashText = null;
    System.out.println("Value to hash::::::::::" + msg);
    byte[] actualKeyBytes = HexToByte(secret_key);

    try {
        m = MessageDigest.getInstance("SHA-256");
        m.update(actualKeyBytes, 0, actualKeyBytes.length);

        try {
            m.update(msg.getBytes("UTF-8"), 0, msg.length());
        } 

        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        hashText = new BigInteger(1, m.digest()).toString(16);

        if (hashText.length() < 64) { //must be 64 in length
            int numberOfZeroes = 64 - hashText.length();
            String zeroes = "";

            for (int i = 0; i < numberOfZeroes; i++) {
                zeroes = zeroes + "0";
            }
            hashText = zeroes + hashText;
        }
    } 

    catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
    }

    hashText = hashText.toUpperCase();
    return hashText;
}

public static byte[] hex2Byte(String str) {
    byte[] bytes = new byte[str.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
        bytes[i] = (byte) Integer
                .parseInt(str.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
}
