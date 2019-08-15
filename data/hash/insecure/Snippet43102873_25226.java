public static void main(String[] args) throws Exception{
    long startTime = System.currentTimeMillis();

    byte[] bytes = createSha1(new File("src\\main\\resources\\200mb_file.zip"));
    System.out.println(new String(bytes));

    long endTime = System.currentTimeMillis();
    long duration = (endTime - startTime);
    System.out.format("Duration: %dms\n", duration);
}

private static byte[] createSha1(File file) throws Exception  {
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    InputStream fis = new FileInputStream(file);
    int n = 0;
    byte[] buffer = new byte[8192];
    while (n != -1) {
        n = fis.read(buffer);
        if (n > 0) {
            digest.update(buffer, 0, n);
        }
    }
    return digest.digest();
}
