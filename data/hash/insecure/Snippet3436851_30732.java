File file = new File("/file.torrent");
MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
InputStream input = null;

try {
    input = new FileInputStream(file);
    StringBuilder builder = new StringBuilder();
    while (!builder.toString().endsWith("4:info")) {
        builder.append((char) input.read()); // It's ASCII anyway.
    }
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    for (int data; (data = input.read()) > -1; output.write(data));
    sha1.update(output.toByteArray(), 0, output.size() - 1);
} finally {
    if (input != null) try { input.close(); } catch (IOException ignore) {}
}

byte[] hash = sha1.digest(); // Here's your hash. Do your thing with it.
