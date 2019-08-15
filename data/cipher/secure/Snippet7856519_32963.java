// JUnit Test
    @Test
    public void testDecrypt() {
        String cipherText = "rrAwZQCAIj19XauZE6tQEg/HQuWB7gw+1uVO0hylyWyCSJo/y7uB6Xj4BRVi+a3qY9GQ/ahjPdUF/kSHptt6QttkvQf89JS13Mo3mRAnaDK/8uoRur8TDuKzLtCSjaMAg72LqObx04+yLd9hI2krtCaWd2saCLP/cWvTQ9oc1xQ=";
        String iv = "o1clHzdEkUV4sFj72VwDFQ==";
        String syncKey = "gbh7teqqcgyzd65svjgibd7tqy";

        SecretKeySpec key = new SecretKeySpec(convertFromBase32(syncKey), "AES");
        byte[] cipherBytes = convertFromBase64(cipherText);
        System.out.println(cipherBytes.length);
        Encrypted d = Crypto.decrypt(new Encrypted(cipherBytes, key,
                convertFromBase64(iv)));
        String decryptedText = new String(d.getCipherText());
    }

// Actual Code
public static Encrypted decrypt(Encrypted encrypted) {
        // Initialize the Cipher
        Cipher cipher = null;
        IvParameterSpec ivParam = new IvParameterSpec(
                encrypted.getInitializationVector());
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, encrypted.getSymmetricKey(),
                    ivParam);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            e1.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        byte[] outputBytes = cryptCommon(cipher, encrypted.getCipherText());
        Encrypted decrypted = new Encrypted(outputBytes,
                encrypted.getSymmetricKey(), cipher.getIV());
        return decrypted;
    }

    private static byte[] cryptCommon(Cipher cipher, byte[] inputBytes) {
        byte[] outputBytes = null;
        try {
            outputBytes = cipher.doFinal(inputBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return outputBytes;
    }
