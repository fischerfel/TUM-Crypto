private static void blockRead(boolean verifyHash) {
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
    out = new BufferedWriter(new FileWriter(BLOCK_FILE));
    char[] buf = new char[COUNT + 1]; // leave a space for the newline
    int lastRead = in.read(buf, 0, COUNT); // read in 309 chars at a time
    while (lastRead != -1) { // end of file
      // technically less than 309 characters may have been read
      // this is very unusual but possible so we need to keep
      // reading until we get all the characters we want
      int totalRead = lastRead;
      while (totalRead < COUNT) {
        lastRead = in.read(buf, totalRead, COUNT - totalRead);
        if (lastRead == -1) {
          break;
        } else {
          totalRead++;
        }
      }

      // if we get -1, it'll eventually signal an exit but first
      // we must write any characters we have read
      // note: it is assumed that the trailing number, which may be
      // less than 309 will still have a newline appended. this may
      // note be the case
      if (totalRead == COUNT) {
        buf[totalRead++] = '\n';
      }
      if (totalRead > 0) {
        out.write(buf, 0, totalRead);
        if (verifyHash) {
          md.update(new String(buf, 0, totalRead).getBytes("UTF-8"));
        }
        wrote += totalRead;
      }

      // don't try and read again if we've already hit EOF
      if (lastRead != -1) {
        lastRead = in.read(buf, 0, 309);
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
