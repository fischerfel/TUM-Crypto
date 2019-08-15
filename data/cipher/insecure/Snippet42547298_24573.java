    String content = "some content";
    String privateKey = "secret key";
    Key key = new SecretKeySpec(privateKey.getBytes(), "AES/ECB/PKCS7Padding");
    try {
        Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        localCipher.init(2, key);
        Log.e("error", new String(localCipher.doFinal(Base64.decode(content, 0))));
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
        e.printStackTrace();
    }
