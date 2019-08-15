static byte[] md5(Path file) {
    try {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        int read;
        byte[] buffer = new byte[4096];
        try (InputStream is = new FileInputStream(file.toFile())) {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
        }
        return digest.digest();
    } catch (IOException | NoSuchAlgorithmException ex) {
        //handle it or
        throw new RuntimeException(ex);
    }
}
public static void main(String[] args) throws IOException {
    System.out.println("first attempt:");
    Files.list(Paths.get("/tmp/t")).forEach(System.out::println);
    System.out.println("second attempt:");
    Files.list(Paths.get("/tmp/t"))
        .collect(Collectors.toMap(f -> new BigInteger(md5(f)), f -> f, (f1, f2) -> f1))
        .values()
        .forEach(System.out::println);
}
