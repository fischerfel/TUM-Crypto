 private void md5Encode(String password) {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return;
    }

    md.update(javaStringToNullTerminatedString(password));

    byte[] digest = md.digest();

    StringBuffer sb = new StringBuffer();
    for (byte b : digest) {
        sb.append(String.format("%02x", b & 0xff));
    }

    System.out.println("original:" + password);
    System.out.println("digested(hex):" + sb.toString());
 }

private byte[] javaStringToNullTerminatedString(String string) {
    CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();

    int len = string.length();
    byte b[] = new byte[len + 1];
    ByteBuffer bbuf = ByteBuffer.wrap(b);
    enc.encode(CharBuffer.wrap(string), bbuf, true);

    b[len] = 0;

    return b;
}
