public static String encode(String src, String key) {
    // This is the function that has the exception. I put it all inside try-catch because an exception here would mean the program cannot work at all

    try {
        SecretKeySpec k = new SecretKeySpec(key.substring(0, 16).getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes());

        CryptoCipher encipher;
        encipher = Utils.getCipherInstance("AES/CBC/PKCS5Padding", getProps());

        byte[] input = src.getBytes();
        byte[] output = new byte[(input.length / 16 + 1) * 16];

        encipher.init(Cipher.ENCRYPT_MODE, k, iv);

        int updateBytes = encipher.update(input, 0, input.length, output, 0);
        int finalBytes = encipher.doFinal(input, 0, 0, output, updateBytes);
        encipher.close();

        return hexString(Arrays.copyOf(output, updateBytes + finalBytes));
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }   
}
