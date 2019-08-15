  // Condensed the message and do MD5
  try {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] data = cleartext.getBytes(ENCODING);
    md.update(data);
    byte[] digestedByteArray = md.digest();
    // Convert digested bytes to 2 chars Hex Encoding
    md5String = HexUtils.bytesToHex(digestedByteArray);

  } catch (NoSuchAlgorithmException ns) {
    ns.printStackTrace();
  } catch (UnsupportedEncodingException ex) {
    ex.printStackTrace();
  }
