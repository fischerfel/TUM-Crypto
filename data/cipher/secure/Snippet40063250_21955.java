public static String encrypt(String data, String initialVectorString, String secretKey) {
        String encryptedData = null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(md5(secretKey).substring(0, 16).getBytes(), "AES");
            IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initialVector);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            byte[] base64encrypted = (new org.apache.commons.codec.binary.Base64()).encode(encrypted);
            encryptedData = new String(base64encrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedData;
    }
