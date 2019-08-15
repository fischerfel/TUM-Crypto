public static void main(String[] args) {
    hash("password");
}

private static String hash(String salted) {
    byte[] digest;
    try {
        MessageDigest mda = MessageDigest.getInstance("SHA-512");
        digest = mda.digest(salted.getBytes("UTF-8"));
    } catch (Exception e) {
        digest = new byte[]{};
    }

    String str = "";
    for (byte aDigest : digest) {
        str += String.format("%02x", 0xFF & aDigest);
    }

    return str;
}
