   public static void doDecrypt( BigInteger  modules, BigInteger  d , String encrypted )
    {
            try {
                    byte[] decodedBytes = Base64.decodeBase64( encrypted );
                    KeyFactory factory = KeyFactory.getInstance("RSA");
                    Cipher cipher = Cipher.getInstance("RSA");

                    RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
                    PrivateKey privKey = factory.generatePrivate(privSpec);

                    cipher.init(Cipher.DECRYPT_MODE, privKey);
                    byte[] decrypted = cipher.doFinal(decodedBytes) ;
                    System.out.println("decrypted: " + new String(decrypted));
            }
            catch (Exception e) {
                    e.printStackTrace();
            }

    }
