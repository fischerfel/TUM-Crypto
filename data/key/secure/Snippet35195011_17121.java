public static String decryptAES(String dataEncrypt, String key) throws Exception {
    synchronized(decryptLock) {
        String dataDecrypted = new String();
        try {
            Cipher aesCipher = getAesCipher();
            byte[] raw = hexToBytes(key);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            aesCipher.init(Cipher.DECRYPT_MODE, skeySpec, aesCipher.getParameters());

            byte[] decordedValue = DatatypeConverter.parseBase64Binary(dataEncrypt);

            byte[] byteDecryptedText = aesCipher.doFinal(decordedValue);
            dataDecrypted = new String(byteDecryptedText);
            return dataDecrypted;
        } catch (Exception ex) {
            log.error("error decryptAES " +ex.getMessage());
        }

        return dataDecrypted;
    }
