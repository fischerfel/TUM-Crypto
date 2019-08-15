byte[] bytes = { 0x35, 0x24, 0x76, 0x12 };
  MessageDigest m = MessageDigest.getInstance("MD5");
  byte[] digest = m.digest(bytes);
