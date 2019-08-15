        private byte[] decrypt(byte[] raw, byte[] encrypted, byte[] iv)
            throws Exception {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
