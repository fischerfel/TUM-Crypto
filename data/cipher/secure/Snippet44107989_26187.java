public static String encryptString(SecretKey key, String plainText) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        //IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");//AES/ECB/PKCS5Padding //"AES/GCM/NoPadding", "BC"
        byte[] plainTextBytes = plainText.getBytes("UTF-8");
        byte[] cipherText;

        //cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        cipher.init(Cipher.ENCRYPT_MODE, key);
        return new String(Base64.getEncoder().encode(cipher.doFinal(plainTextBytes)));
      }



           public static String decryptString(SecretKey key, String 
          cipherText) throws NoSuchProviderException, 
          NoSuchAlgorithmException, NoSuchPaddingException, 
          InvalidKeyException, InvalidAlgorithmParameterException, 
          IllegalBlockSizeException, BadPaddingException, 
          UnsupportedEncodingException, ShortBufferException {


        Key decryptionKey = new SecretKeySpec(key.getEncoded(),
                key.getAlgorithm());
       IvParameterSpec ivSpec = new IvParameterSpec(ivString.getBytes("UTF-8"));
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");//AES/GCM/NoPadding", "BC");

        cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
        return new String (Base64.getEncoder().encode(cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes()))));

    }
