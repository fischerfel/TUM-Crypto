private String doEncrypt3DES(String key, String data) throws Exception{
        SecretKey secretKey;
        byte[] keyValue;
        Cipher c;

        keyValue = Hex.decodeHex(key.toCharArray());
        DESedeKeySpec keySpec = new DESedeKeySpec(keyValue);
        secretKey = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);

        // Create the cipher
        c = Cipher.getInstance("DESede/ECB/NoPadding");

        c.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] text = data.getBytes("utf-8"); // Base64.decodeBase64(data);
        byte[] textEncrypt = c.doFinal(text);
        String hex = bytesToHex(textEncrypt);
        return hex;
}
