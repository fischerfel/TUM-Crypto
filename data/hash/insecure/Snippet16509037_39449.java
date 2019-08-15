public static String getEncryptedValue(String str) {
    String password = str;
    String algorithm = "SHA";
    byte[] plainText = password.getBytes();
    try {
        md = MessageDigest.getInstance(algorithm);
    } catch (Exception e) {
        e.printStackTrace();
    }

    md.reset();
    md.update(plainText);
    byte[] encodedPassword = md.digest();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < encodedPassword.length; i++) {
        if ((encodedPassword[i] & 0xff) < 0x10) {
            sb.append("0");
        }

        sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
    }

    return sb.toString();
}
