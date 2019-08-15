public static String encryptRSAToString(String text, String strPublicKey) {

        byte[] cipherText = null;
        String strEncryInfoData="";
        try {

        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(strPublicKey.trim().getBytes()));
        Key publicKey = keyFac.generatePublic(keySpec);

        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance("RSA");
        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        cipherText = cipher.doFinal(text.getBytes());
        strEncryInfoData = new String(Base64.encodeBase64(cipherText)); 

        } catch (Exception e) {
        e.printStackTrace();
        }
        return strEncryInfoData.replaceAll("(\\r|\\n)", "");
    }
