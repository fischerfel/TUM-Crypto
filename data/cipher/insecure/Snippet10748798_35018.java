    protected String encrypt(String token) throws Exception {
            // Instantiate the cipher
            final SecretKeySpec key = new SecretKeySpec("oldhouse".getBytes("ISO-8859-1"), "DES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec("houseold".getBytes());

    //function used to encrypt
    protected String encrypt(String token) throws Exception {
            // Instantiate the cipher
            final SecretKeySpec key = new SecretKeySpec("oldhouse".getBytes("ISO-8859-1"), "DES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec("houseold".getBytes());

            Cipher cipher = Cipher.getInstance("DES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            byte[] binaryData = cipher.doFinal(token.getBytes("ISO-8859-1"));

            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(binaryData), "ISO-8859-1");

        }
