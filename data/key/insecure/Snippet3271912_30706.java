public static Key getKey(int cipher) {
    if (cipher == 0) {
      return new SecretKeySpec(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 }, "DES");
    }
    return new SecretKeySpec(new byte[] { 2, 2, 2, 2, 2, 2, 2, 2 }, "DES");
  }
