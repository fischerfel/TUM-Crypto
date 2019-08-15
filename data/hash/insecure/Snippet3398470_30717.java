private static void charRead(boolean verifyHash) {
  Reader in = null;
  Writer out = null;
  long start = System.nanoTime();
  long wrote = 0;
  MessageDigest md = null;
  try {
    if (verifyHash) {
      md = MessageDigest.getInstance("SHA1");
    }
    in = new BufferedReader(new FileReader(IN_FILE));
    out = new BufferedWriter(new FileWriter(CHAR_FILE));
    int count = 0;
    for (int c = in.read(); c != -1; c = in.read()) {
      if (verifyHash) {
        md.update((byte) c);
      }
      out.write(c);
      wrote++;
      if (++count >= COUNT) {
        if (verifyHash) {
          md.update((byte) '\n');
        }
        out.write("\n");
        wrote++;
        count = 0;
      }
    }
  } catch (IOException e) {
    throw new RuntimeException(e);
  } catch (NoSuchAlgorithmException e) {
    throw new RuntimeException(e);
  } finally {
    safeClose(in);
    safeClose(out);
    long end = System.nanoTime();
    System.out.printf("Created %s size %,d in %,.3f seconds. Hash: %s%n",
        CHAR_FILE, wrote, (end - start) / 1000000000.0d, hash(md, verifyHash));
  }
}
