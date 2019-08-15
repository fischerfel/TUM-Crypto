public static byte[] encrypt3DESECB(byte[] keyBytes, byte[] dataBytes) {
    try {
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey);
        System.out.println("function called");
        return cipher.doFinal(dataBytes);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}           
