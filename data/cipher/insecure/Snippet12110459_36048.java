private static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
private static int KEY_LENGTH = 64;

 public static SecretKey deriveKeyDES() {
        try {
            long start = System.currentTimeMillis();

            KeyGenerator kgen = KeyGenerator.getInstance("DES");
            kgen.init(KEY_LENGTH);
            SecretKey result = kgen.generateKey();

            long elapsed = System.currentTimeMillis() - start;
            return result;

        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } 
    }


    public static String encrypt(String plaintext, SecretKey key) {
        try {

            long start = System.currentTimeMillis();
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));

            long elapsed = System.currentTimeMillis() - start;

            return toBase64(cipherText);

        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP).trim();
    }


    public static String decrypt(String ciphertext, SecretKey key) {
        try {
            byte[] cipherBytes = fromBase64(ciphertext);

                long start = System.currentTimeMillis();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, key);
            cipher.update(cipherBytes);

             // This is where I get exception
            byte[] plaintext = cipher.doFinal(cipherBytes);

            String plainrStr = new String(plaintext, "UTF-8").trim();
            long elapsed = System.currentTimeMillis() - start;

            return plainrStr;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }
