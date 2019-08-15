public static String encrypt(byte[] input, String key) {
    try {
        byte[] iv = new byte[16];

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        String newKey = "";
        if (key.length() >= 32) {
            newKey = key.substring(0, 32);
        } else {
            for (int i = key.length(); i < 32; i++) {
                key += "0";
            }
            newKey = key.substring(0, 32);
        }

        SecretKeySpec skeySpec = new SecretKeySpec(newKey.getBytes(), "AES");
        //skeySpec = new SecretKeySpec(newKey.getBytes(), 0, newKey.length(),  "AES");
        Cipher fileCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        fileCipher.init(1, skeySpec, paramSpec);

        byte[] decrypted = fileCipher.doFinal(input);

        byte[] base64enc = Base64.encode(decrypted, 0);

        return new String(base64enc);
    } catch (Exception e) {
        Log.e("Exception", e.getMessage());
    }
    return null;
}

public static byte[] decrypt(String input, String key) {
    try {
        byte[] iv = new byte[16];

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        byte[] base64enc = Base64.decode(input.getBytes(), 0);

        String newKey = "";
        if (key.length() >= 32) {
            newKey = key.substring(0, 32);
        } else {
            for (int i = key.length(); i < 32; i++) {
                key += "0";
            }
            newKey = key.substring(0, 32);;
        }

        SecretKeySpec skeySpec = new SecretKeySpec(newKey.getBytes(), "AES");
        Cipher fileCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        fileCipher.init(2, skeySpec, paramSpec);

        int x = base64enc.length;

        return fileCipher.doFinal(base64enc);

    } catch (Exception e) {
        Log.e("Exception: ", e.getMessage());
    }
    return null;
}
