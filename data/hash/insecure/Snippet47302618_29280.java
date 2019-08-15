static void updateDigestWithLong(MessageDigest md, long l) {
  md.update((byte)l);
  md.update((byte)(l >> 8));
  md.update((byte)(l >> 16));
  md.update((byte)(l >> 24));
}

// this is from the Guava sources, can reimplement if you prefer
static long padToLong(bytes[] bytes) {
  long retVal = (bytes[0] & 0xFF);
  for (int i = 1; i < Math.min(bytes.length, 8); i++) {
    retVal |= (bytes[i] & 0xFFL) << (i * 8);
  }
  return retVal;
}

static long hashLongsToLong(long primary, long secondary) {
  MessageDigest md = MessageDigest.getInstance("SHA-1");
  updateDigestWithLong(md, primary);
  updateDigestWithLong(md, secondary);
  return padToLong(md.digest());
}
