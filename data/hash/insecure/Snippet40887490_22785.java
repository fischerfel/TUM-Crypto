 private String _encrypt(String message, String secretKey) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 16);

        SecretKey key = new SecretKeySpec(keyBytes, "DESede/ECB/PKCS7Padding");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainTextBytes = message.getBytes("utf-8");


        byte[] buf = cipher.doFinal(plainTextBytes);
        byte [] base64Bytes = Base64.encodeBase64(buf);
        String base64EncryptedString = new String(base64Bytes);

        return base64EncryptedString;
    }
