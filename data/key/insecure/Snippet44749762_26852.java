private static final String DEFAULT_ALGORITHM = "HMACSHA256";
@Override
  public synchronized byte[] sign(byte[] payload) {
    SecretKey sk = new SecretKeySpec("secret".getBytes(), DEFAULT_ALGORITHM);
    try {
        mac.init(sk);
        return mac.doFinal(payload);
    } catch (GeneralSecurityException e) {
        throw new RuntimeException(e);
    }

  }
