public static String getHash(File file) throws FileNotFoundException, IOException {
    if(file == null || !file.isFile()) {
        return "";
    }
    FileInputStream in = null;
    try {
        in = new FileInputStream(file);
        byte [] bytes = toByteArray(in);
        return getHash(bytes);
    } catch(Exception e) {
        Logging.log("Unable to get MD5 hash for file: " + ile.getName());
    } finally {
        StreamUtils.close(in);
    }
    return "";
}

public static String getHash(byte[] bytes) {
    MessageDigest digest = getMessageDigest();
    byte[] hash = digest.digest(bytes);
    StringBuilder builder = new StringBuilder();
    for (int val : hash) {
        builder.append(Integer.toHexString(val & 0xff));
    }
    return builder.toString();
}

private static MessageDigest getMessageDigest() {
    try {
        return MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
