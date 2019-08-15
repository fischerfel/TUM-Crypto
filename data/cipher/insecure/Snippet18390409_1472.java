try {
       SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
       byte[] salt = new byte[10];
       secureRandom.nextBytes(salt);
       byte[] encryptedPassword = //some method to mix salt with plain password
       Cipher cipher = Cipher.getInstance("AES");
       SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
       cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

       return cipher.doFinal(encryptedPassword);

     } catch (NoSuchAlgorithmException |
              UnsupportedEncodingException |
              NoSuchPaddingException |
              InvalidKeyException |
              IllegalBlockSizeException |
              BadPaddingException ex) {
                 //Logger...
     }
