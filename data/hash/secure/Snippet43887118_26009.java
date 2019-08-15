public static boolean isPassValid(){
    String hash = bin2hex(getHash("password"));
    return hash.equals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
}

private static byte[] getHash(String password) {
    MessageDigest digest=null;
    try {
        digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e1) {
        e1.printStackTrace();
    }
    assert digest != null;
    digest.reset();
    return digest.digest(password.getBytes());
}

private static String bin2hex(byte[] data) {
    return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
}
