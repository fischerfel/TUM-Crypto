public static String decryptKey(String dataForDecryption) {

        Cipher cipher;
        byte[] decryptedData = null;
        final byte[] initVector = javax.xml.bind.DatatypeConverter.parseHexBinary("629E2E1500B6BA687A385D410D5B08E3");
        try {
            if (dataForDecryption != null) {
                Key key = getKeyFromKeyStore(); //This method will return the key from keystore.
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKey secKey = new SecretKeySpec(key.getEncoded(), ENCRYPTION_TYPE);

                cipher.init(Cipher.DECRYPT_MODE, secKey, new IvParameterSpec(initVector, 0, cipher.getBlockSize()));
                decryptedData = cipher.doFinal(Base64.decodeBase64(dataForDecryption.getBytes()));
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            LOG.error("Error getting in decrypting the ASE Key:");
            throw new IllegalArgumentException("Decryption failed ::" + e);
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String(decryptedData);

}
