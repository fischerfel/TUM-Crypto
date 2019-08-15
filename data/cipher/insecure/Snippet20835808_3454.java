public static byte[] decrypt3DESCBC(byte[] keyBytes, byte[] ivBytes,
        byte[] dataBytes) {
    try {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec); // Causes Exception
        return cipher.doFinal(dataBytes);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
