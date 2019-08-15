private static byte[] encryptData(ByteArrayOutputStream data, byte[] symmetricKey) throws EncryptionException {
        try {
            SecretKey secKey = new SecretKeySpec(symmetricKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secKey);
            return cipher.doFinal(data.toByteArray());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                InvalidKeyException |
                BadPaddingException e) {
            throw new EncryptionException(e);
        }
    }
