public static String encriptB64Aes(String value, String aesKey) {
    String result = null;
    try {

       Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

       //in PHP !?
       SecretKey secretKey = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
       IvParameterSpec ivParameterSpec = new IvParameterSpec(secretKey.getEncoded());

       encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

       // Encrypt
       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
       CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
       cipherOutputStream.write(value.getBytes());
       cipherOutputStream.flush();
       cipherOutputStream.close();
       byte[] encryptedBytes = outputStream.toByteArray();
       result = encodeBase64(encryptedBytes);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
       e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return result;
}
