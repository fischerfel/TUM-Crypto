public static String encrypt(String key, String initVector, String value) {
    try {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(value.getBytes());
        System.out.println("encrypted string: "
                + Base64.encodeToString(encrypted, Base64.DEFAULT));

        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return null;
}
