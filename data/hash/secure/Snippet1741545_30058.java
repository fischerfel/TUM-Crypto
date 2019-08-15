int buff = 16384;
try {
    RandomAccessFile file = new RandomAccessFile("T:\\someLargeFile.m2v", "r");

    long startTime = System.nanoTime();
    MessageDigest hashSum = MessageDigest.getInstance("SHA-256");

    byte[] buffer = new byte[buff];
    byte[] partialHash = null;

    long read = 0;

    // calculate the hash of the hole file for the test
    long offset = file.length();
    int unitsize;
    while (read < offset) {
        unitsize = (int) (((offset - read) >= buff) ? buff : (offset - read));
        file.read(buffer, 0, unitsize);

        hashSum.update(buffer, 0, unitsize);

        read += unitsize;
    }

    file.close();
    partialHash = new byte[hashSum.getDigestLength()];
    partialHash = hashSum.digest();

    long endTime = System.nanoTime();

    System.out.println(endTime - startTime);

} catch (FileNotFoundException e) {
    e.printStackTrace();
}
