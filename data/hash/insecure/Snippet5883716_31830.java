public static void main(String args[]) {
    try {
        File f1 = new File("\\\\79.129.94.116\\share\\bots\\triplon_bots.jar");
        File f2 = new File("\\\\triplon\\share\\bots\\triplon_bots.jar");
        System.out.println(f1.getCanonicalPath().equals(f2.getCanonicalPath()));
        System.out.println(computeDigestOfFile(f1).equals(computeDigestOfFile(f2)));
    }
    catch(Exception e) {
        e.printStackTrace();
    }
}

private static String computeDigestOfFile(File f) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    InputStream is = new FileInputStream(f);
    try {
        is = new DigestInputStream(is, md);
        byte[] buffer = new byte[1024];
        while(is.read(buffer) != -1) {
            md.update(buffer);
        }
    }
    finally {
        is.close();
    }
    return new BigInteger(1,md.digest()).toString(16);
}
