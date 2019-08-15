public static String decrypt(String encryptedSecretKeyString ,String encryptedTextString)
{
     try {

          // 1. Get private key
          PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(Base64.decode(AppConstant.privateKey, Base64.DEFAULT));
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          PrivateKey privateKey = keyFactory.generatePrivate(privateSpec);

          // 2. Decrypt encrypted secret key using private key
          Cipher cipher1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");   
          cipher1.init(Cipher.DECRYPT_MODE, privateKey);  
          byte[] secretKeyBytes = cipher1.doFinal(Base64.decode(encryptedSecretKeyString, Base64.DEFAULT));//here i am getting error.
          SecretKey secretKey = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "AES");

          // 3. Decrypt encrypted text using secret key
          byte[] raw = secretKey.getEncoded();
          SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
          Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
          cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
          byte[] original = cipher.doFinal(Base64.decode(encryptedTextString, Base64.DEFAULT));
          String text = new String(original, Charset.forName("UTF-8"));

          // 4. Print the original text sent by client
          System.out.println("text\n" + text + "\n\n");
          return text;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
          e.printStackTrace();
        }
    return "null";
}
