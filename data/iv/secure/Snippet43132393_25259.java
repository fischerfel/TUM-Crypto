public String Encrypt(String str, String desKey, String mode) {
    try {
        KeySpec keySpec = null;
        SecretKey key = null;
        Cipher ecipher = null;
        if (desKey.length() == 8) {
            keySpec = new DESKeySpec(desKey.getBytes("UTF8"));
            key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            if(mode.equals(ECB)){
                ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                ecipher.init(Cipher.ENCRYPT_MODE, key);
            }else if (mode.equals(CBC)){
                ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                ecipher.init(Cipher.ENCRYPT_MODE, key,ivSpec);
            }
        } else if (desKey.length() == 24) {
            keySpec = new DESedeKeySpec(desKey.getBytes("UTF8"));
            key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
            ecipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
        }

        byte[] data = str.getBytes("UTF-8");
        byte[] crypt = ecipher.doFinal(data);

        return Base64.encodeToString(crypt, 0);
    } catch (Exception ex) {
        Log.d("ZVM", ex.getMessage());
    }
    return null;
}
