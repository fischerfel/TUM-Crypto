public static String getMD5ChecksumByFlash(String filename) throws Exception {
    InputStream fis = new FileInputStream(filename);
    byte[] buffer = new byte[1];
    MessageDigest complete = MessageDigest.getInstance("MD5");

    int passes = fis.available() / 100;
    int currentOffset = 0;
    int readBytes = -1;

    do {
        System.out.println("0a "+currentOffset);
        System.out.println("0b "+readBytes);
        readBytes = fis.read(buffer, currentOffset, 1);
        System.out.println("1 "+currentOffset);
        System.out.println("2 "+readBytes);
        if ( readBytes!=-1 ) {
            complete.update(buffer, 0, readBytes);
            currentOffset += passes;
            System.out.println("4 "+readBytes);
        }
        System.out.println("3 "+currentOffset);
        System.out.println("5 "+readBytes);
    } while ( readBytes!=-1 );
    fis.close();

    byte[] b = complete.digest();
    String result = "";
    for (int i = 0; i < b.length; i++) {
        result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
}
