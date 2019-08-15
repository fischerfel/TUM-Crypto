    private String decrypt (String encryptedText) {
        byte[] clearText = null;
        try {
            SecretKeySpec ks = new SecretKeySpec(getKey(), "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            clearText = c.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
            return new String(clearText, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }
