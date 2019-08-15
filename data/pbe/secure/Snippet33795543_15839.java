 @SuppressWarnings("static-access")
public String decrypt(String encryptedText) throws Exception {

    byte[] saltBytes = salt.getBytes("UTF-8");
    byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);

    // Derive the key
    SecretKeyFactory factory =     SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), 
            saltBytes, 
            pswdIterations, 
            keySize
            );

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    // Decrypt the message
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));


    byte[] decryptedTextBytes = null;
    try {
        decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return new String(decryptedTextBytes);
  }
