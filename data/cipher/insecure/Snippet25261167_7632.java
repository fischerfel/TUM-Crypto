    private static final String AES = "AES";

    private static String encrypt(final String strKey, final String strToEncrypt) {
        SecretKeySpec secKeySpec = null;
        Cipher cipher = null;
        byte[] encrypted = null;
        try {
            secKeySpec = new SecretKeySpec(strKey.getBytes(), "AES");
            cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secKeySpec);
            encrypted = cipher.doFinal(strToEncrypt.getBytes());

        } catch (final Exception e) {
            System.out.println(e);
        }
        return Base64.encodeBase64String(encrypted);
    }
