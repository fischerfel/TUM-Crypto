    public static void check() {
    if (isJarFile()) {
        try (Scanner s = new Scanner(new URL(HASH_PROVIDER).openStream())) {
            String remote_hash = s.nextLine().trim();
            File jarFile = getJarFile();
            if (jarFile != null && !remote_hash.equals(getMD5Checksum(jarFile.getAbsolutePath()))) {
                jarFile.setWritable(true);
                jarFile.deleteOnExit();
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

public static byte[] createChecksum(String filename) throws Exception {
    InputStream fis = new FileInputStream(filename);
    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;
    do {
        numRead = fis.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }
    } while (numRead != -1);
    fis.close();
    return complete.digest();
}

public static String getMD5Checksum(String filename) throws Exception {
    byte[] b = createChecksum(filename);
    String result = "";
    for (int i = 0; i < b.length; i++) {
        result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
}

public static File getJarFile() {
    try {
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
    return null;
}
