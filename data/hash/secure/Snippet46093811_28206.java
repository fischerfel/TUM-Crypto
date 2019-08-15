 public static String digest(String string, String salt) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();
    digest.update(salt.getBytes("UTF-8"));

    byte[] btPass = digest.digest(string.getBytes("UTF-8"));
    int iter = 1000;
    for (int i = 0; i < iter; i++) {
        digest.reset();
        btPass = digest.digest(btPass);
    }
    HexBinaryAdapter adapter = new HexBinaryAdapter();
    return adapter.marshal(btPass);
}
