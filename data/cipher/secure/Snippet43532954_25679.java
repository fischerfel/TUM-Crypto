  public static String encryptDataRSA(final String data) throws IOException {
        final byte[] dataToEncrypt = data.getBytes();
        byte[] encryptedData = null;

        try {

            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(RSAUtils.PUBLIC_KEY.getBytes()));

            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedData = cipher.doFinal(dataToEncrypt);

            try {
                final String encryptedText = new String(Base64.encode(encryptedData, Base64.DEFAULT), "UTF-8");
                return encryptedText.toString();
            }
            catch (final UnsupportedEncodingException e1) { return null; }
        } catch (Exception e) { e.printStackTrace(); }

        return "ERROR";
    }
