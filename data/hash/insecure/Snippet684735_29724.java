  MessageDigest md = MessageDigest.getInstance("MD5");
  ByteBuffer bb = ByteBuffer.allocate(4 * bimg.getWidth());
  for (int y = bimg.getHeight()-1; y >= 0; y--) {
    bb.clear();
    for (int x = bimg.getWidth()-1; x >= 0; x--) {
      bb.putInt(bimg.getRGB(x, y));
    }
    md.update(bb.array());
  }
  byte[] digBytes = md.digest();
