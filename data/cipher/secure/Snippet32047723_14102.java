private static final Provider BC_PROVIDER = new BouncyCastleProvider();

...

Cipher rsaCipher = Cipher.getInstance("RSA/NONE/NoPadding", BC_PROVIDER);
