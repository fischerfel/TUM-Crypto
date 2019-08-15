public String decrypt(String enc) throws Exception {
    try {
        String key = "ffce885876a617e7";
        String vector = "9ee153a3df56965e7baf13a7fa1075cc";

        IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(vector.getBytes(),
                "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return new String(cipher.doFinal(hexToByte(enc)), "UTF-8");

    } catch (Exception localException) {
        throw new Exception("[decrypt] " + localException.getMessage());
    }
}

private static byte[] hexToByte(String enc) {
    int j = enc.length() / 2;
    byte[] arrayOfByte = new byte[j];

    if (j >= 2) {
        for (int i = 0; i < j; i++) {
            arrayOfByte[i] = ((byte) Integer.parseInt(
                    enc.substring(i * 2, 2 + i * 2), 16));
        }
    }
    return arrayOfByte;
}
