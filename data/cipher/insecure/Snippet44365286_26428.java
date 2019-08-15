public static String DESDecrypt(String src, String password) {
    String result = src;
    if (src == null) {
        return result;
    }

        if (src.isEmpty()) {
            return result;
        }

        byte[] item = password.substring(0, 8).getBytes();
        try {
            DESKeySpec item2 = new DESKeySpec(item);
            SecretKey securekey = SecretKeyFactory.getInstance("DES").generateSecret(item2);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block

        }

        return password.substring(0, 8);
        /*AlgorithmParameterSpec iv = new IvParameterSpec(ivkey);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(2, securekey, iv);
        return new String(cipher.doFinal(Base64.getDecoder().decode(src)));*/

}
