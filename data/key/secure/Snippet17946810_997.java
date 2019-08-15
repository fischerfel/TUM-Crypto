  try {
        byte [] encryptionKeyBytes = md5EncryptionKey.getBytes("UTF-8");
        Key key = new SecretKeySpec(encryptionKeyBytes, "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new Base64().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    } catch (InvalidKeyException e) {
        log.error "[getDecryptedValue] InvalidKeyException: " + e
    } catch (IllegalBlockSizeException e) {
        log.error "[getDecryptedValue] InvalidKeyException: " + e
    } catch (BadPaddingException e) {
        log.error "[getDecryptedValue] InvalidKeyException: " + e
    } catch (NoSuchAlgorithmException e) {
        log.error "[getDecryptedValue] InvalidKeyException: " + e
    } catch (NoSuchPaddingException e) {
        log.error "[getDecryptedValue] InvalidKeyException: " + e
    } catch (Exception e) {
        log.error "[getDecryptedValue] InvalidKeyException: " + e
    }
