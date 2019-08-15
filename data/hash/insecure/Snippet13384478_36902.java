public static String checkHashURL(String input) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new URL(input).openStream();

        try {
            is = new DigestInputStream(is, md);

            int b;

            while ((b = is.read()) > 0) {
                ;
            }
        } finally {
            is.close();
        }
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < digest.length; i++) {
            sb.append(
                    Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(
                            1));
        }
        return sb.toString();

    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}
