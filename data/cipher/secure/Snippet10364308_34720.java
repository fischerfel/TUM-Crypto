    private void test() {

        try {
            String stringMessage="GEQRpAPA577ks/QveudNkk7H9DjItKGLDYW6xhH1YJGabCVzrkejkBh6S+APwEXxB84UV/q0sO5rqkgXWONJQ8CoMTfqXtUkAAwkYHSc86eGewkM8WpctA0AyNVFonOxDCXm84Uq8JRMzqskSH5VXHmMxvHIvpFgdhmt9Ir0cKWzoLsuvgfY9hfypfEyBXGZcoptQeKhsZxRGIlxbXhrFl/LqhC+F6vYtZ/j5pv2LUP38wh2rTCKnAQ+xvC+7wn5SVzt/Wbr/q7GjCoJuU9uFHQSS49KQDt+BzJL2XNwAMmdbC+XHYkEBBWxVSS+0hdSQxoaKVZZJk4hTnHwQlBAkw==";
            //Convert String back to Byte[] and decrpt
            byte[] byteMessage = Base64.decodeBase64(stringMessage.getBytes("UTF-8"));
            System.out.println("ENCRYPTED MESSAGE byte Length: "+byteMessage.length);

            String decryptedMsg = decryptString(byteMessage, loadCASPrivateKey());
            System.out.println(decryptedMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static String decryptString(byte[] message, Key privateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance("RSA/None/NoPadding","BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] cipherData = cipher.doFinal(message);
        return new String(cipherData, "UTF-8");
    }

    private PrivateKey loadCASPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream is = getClass().getResourceAsStream( "/keys/app-private.key" );
        if (is == null) {
            System.out.println("NULL");
        }
        byte[] encodedPrivateKey = new byte[(int) 1216];
        is.read(encodedPrivateKey);
        is.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return privateKey;

    }
