 /**
   * Generates SHA-1 digest of the provided data.
   * 
   * @param data the data to digest
   * @return SHA-1 digest of the provided data.
   */
  public static byte[] sha1Digest(byte[] data) {
    MessageDigest mdSha1 = null;
    try {
      mdSha1 = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e1) {
      Log.e(LOG_TAG, "Error initializing SHA1 message digest");
    }
    mdSha1.update(data);
    byte[] sha1hash = mdSha1.digest();
    return sha1hash;
  }
