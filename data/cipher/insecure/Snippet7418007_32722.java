public static String encrypt(String src, String key) {
    try {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec dks = new DESKeySpec(key.substring(0, 8).getBytes());

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(key.substring(0, 8)
                .getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
        StringBuilder builder = new StringBuilder();
        byte[] bytes = cipher.doFinal(src.getBytes("UTF-8"));
        for (byte b : bytes) {
            System.out.println(b);
                            builder.append(b);
        }

        return builder.toString().toUpperCase();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}
