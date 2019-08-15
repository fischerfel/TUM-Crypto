private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

public static String getMD5(File file) throws NoSuchAlgorithmException, IOException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    InputStream is = new FileInputStream(file);

    try {
        is = new DigestInputStream(is, md);

        byte[] buffer = new byte[4 * 1024];
        int read;

        while ((read = is.read(buffer)) != -1) {
            md.update(buffer, 0, read);
        }

    } finally {
        is.close();
    }

    final byte[] data = md.digest();
    final int l = data.length;
    final char[] out = new char[l << 1];

    for (int i = 0, j = 0; i < l; i++) {
        out[j++] = HEX_DIGITS[(0xF0 & data[i]) >>> 4];
        out[j++] = HEX_DIGITS[0x0F & data[i]];
    }

    return new String(out);
}
