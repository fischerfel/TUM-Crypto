 public static String encrypt(String key, String iv, String data) {
        try {
            IvParameterSpec initVector = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(WebviewActivity.CIPHER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector);

            byte[] encryptedData = cipher.doFinal((data.getBytes()));

            String base64_EncryptedData = Base64.encodeToString(encryptedData,Base64.DEFAULT);

            return base64_EncryptedData;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
