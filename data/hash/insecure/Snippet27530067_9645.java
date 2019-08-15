    MessageDigest digest;
    try {
        digest = MessageDigest.getInstance("MD5");
        byte[] md5key = digest.digest(key.getBytes("UTF-8"));
        Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
        SecretKeySpec myKey = new SecretKeySpec(md5key, "DESede");
        cipher.init(Cipher.ENCRYPT_MODE, myKey);

        try {
            byte[] encryptedPlainText = cipher.doFinal(input.getBytes());

            String encrypted = Base64.encodeToString(encryptedPlainText, 0);
            return encrypted;

        }
    }
