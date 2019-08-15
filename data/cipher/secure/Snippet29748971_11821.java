public String encrypt(String message, String Modulus, String Exponent) {
        String outputEncrypted = "";
        try {
             byte[] modulusBytes = Base64Coder.decode(Modulus);
               byte[] exponentBytes = Base64Coder.decode(Exponent);
               BigInteger modulus = new BigInteger(modulusBytes );               
               BigInteger exponent = new BigInteger(exponentBytes);

               RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
               KeyFactory fact = KeyFactory.getInstance("RSA");
               PublicKey pubKey = fact.generatePublic(rsaPubKey);

               Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
               cipher.init(Cipher.ENCRYPT_MODE, pubKey);

               byte[] plainBytes = new String("abc").getBytes("UTF-8");
               byte[] cipherData = cipher.doFinal( plainBytes );
               String encryptedString = new String(Base64Coder.encode(cipherData));
               Log.i(this.getClass().getSimpleName(), "encryptedString : "+encryptedString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return outputEncrypted;
    }
