public final class Encryption {
    private static final String CHIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String GENERATE_KEY__ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String GENERATE_KEY_ALGORITHM = "AES";
    public static final int CRYPTO_TYPE_ENCRYPT = 0;
    public static final int CRYPTO_TYPE_DECRYPT = 1;

    public static String crypto(String inString, int type, String hashKey, String salt, String charset) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CHIPHER_TRANSFORMATION);
            byte[] inputByte = inString.getBytes(charset);
            switch (type) {
                case CRYPTO_TYPE_DECRYPT:
                    cipher.init(Cipher.DECRYPT_MODE, initKey(hashKey, salt));
                    return new String(cipher.doFinal(Base64.decode(inputByte, Base64.DEFAULT)));
                case CRYPTO_TYPE_ENCRYPT:
                    cipher.init(Cipher.ENCRYPT_MODE, initKey(hashKey, salt));
                    return new String(Base64.encode(cipher.doFinal(inputByte), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static SecretKey getSecretKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(GENERATE_KEY__ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, salt, 1024, 128);
        SecretKey tmp = factory.generateSecret(spec);
        return (new SecretKeySpec(tmp.getEncoded(), GENERATE_KEY_ALGORITHM));
    }

    private static SecretKey initKey(String hashKey, String salt) {
        try {
            return getSecretKey(hashKey.toCharArray(), salt.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
