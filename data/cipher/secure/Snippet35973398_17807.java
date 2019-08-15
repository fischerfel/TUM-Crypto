 private static final String KEY_DERIVATION_FUNCTION = "PBKDF2WithHmacSHA256";
  private static final String ENCRYPTION_ALGORITHM    = "AES";
  private static final String TRANSFORMATION          = "AES/GCM/NoPadding";
  private static final String GENERATOR_ALGORITHM     = "SHA1PRNG";

  private static final int KEY_SIZE_IN_BITS = 128;
  private static final int  IV_SIZE_IN_BITS = 128;
  private static final int TAG_SIZE_IN_BITS = 128;
  private static final int ITERATION_COUNT  = 200_000;





private static Cipher initCipher(int mode,char [] password, byte[] iv) {

    GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_SIZE_IN_BITS, iv);

    SecretKey key = deriveKey(password, iv);
    try {

      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(mode, key, gcmSpec);
      return cipher;
    }

    catch (
      NoSuchAlgorithmException |
      NoSuchPaddingException   |
      InvalidKeyException      |
      InvalidAlgorithmParameterException ex
    ) {
      throw new AssertionError(ex);
    }
 }
