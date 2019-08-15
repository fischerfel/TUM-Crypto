  private IvParameterSpec generateAESIV() {
    // build the initialization vector (randomly).
    SecureRandom random = new SecureRandom();
    byte iv[] = new byte[16];//generate random 16 byte long
    random.nextBytes(iv);
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    return ivspec;
  }
