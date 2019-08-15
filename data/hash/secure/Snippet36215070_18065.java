public static String getFileHashSHA256(String fileName) throws Exception {

    int buff = 16384;

    RandomAccessFile file = new RandomAccessFile(fileName, "r");
    MessageDigest hashSum = MessageDigest.getInstance("SHA-256");

    byte[] buffer = new byte[buff];
    byte[] partialHash = null;

    long read = 0;

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

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < partialHash.length; i++) {
        sb.append(Integer.toString((partialHash[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();

}
