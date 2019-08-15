public static String encryptMD5_Salt(String content) {
    String resultString = "";
    String appkey = "acdf,kef";

    byte[] a = appkey.getBytes();
    byte[] datSource = content.getBytes();
    byte[] b = new byte[a.length + 4 + datSource.length];

    int i;
    for (i = 0; i < datSource.length; i++) {
        b[i] = datSource[i];
    }

    b[i++] = (byte) 143;
    b[i++] = (byte) 112;
    b[i++] = (byte) 131;
    b[i++] = (byte) 143;

    for (int k = 0; k < a.length; k++) {
        b[i] = a[k];
        i++;
    }

    try {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(b);
        resultString = new HexBinaryAdapter().marshal(md5.digest());
    } catch (Exception e) {
        e.printStackTrace();
    }

    return resultString.toLowerCase();
}
