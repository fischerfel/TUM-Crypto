   public static byte[] encrypt(BigInteger modulus, BigInteger exp, byte[] data) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exp);
            PublicKey publicKey = kf.generatePublic(keySpec);
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            FileLog.e("module" , e);
        }
        return null;
    }
