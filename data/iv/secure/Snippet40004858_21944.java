private static String KEY = "0123456789012345";
public static String decrypt(String encrypted_encoded_string) throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

      String plain_text = "";
      try{
          byte[] encrypted_decoded_bytes = Base64.getDecoder().decode(encrypted_encoded_string);
          String encrypted_decoded_string = new String(encrypted_decoded_bytes);
          String iv_string = encrypted_decoded_string.substring(0,16); //IV is retrieved correctly.

          IvParameterSpec iv = new IvParameterSpec(iv_string.getBytes());
          SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

          Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
          cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

          plain_text = new String(cipher.doFinal(encrypted_decoded_bytes));//Returns garbage characters
          return plain_text;

      }  catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
      }

      return plain_text;
 }
