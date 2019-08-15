private static char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
StringBuffer messagebuffer = new StringBuffer();

private String makeHTTPSPostRequest(String httpsURL) throws IOException,
        NoSuchAlgorithmException, CertificateException,
        CertificateEncodingException {
    final HttpsURLConnection con = (HttpsURLConnection) new URL(httpsURL)
            .openConnection();
    con.connect();
    final Certificate cert = con.getServerCertificates()[0];
    final MessageDigest md = MessageDigest.getInstance("SHA1");
    md.update(cert.getEncoded());
    InputStream inputStream = con.getInputStream();
    DataInputStream dis = new DataInputStream(inputStream);
    int ch;
    long len = con.getContentLength();
    if (len != -1) {
        for (int i = 0; i < len; i++)
            if ((ch = dis.read()) != -1) {
                messagebuffer.append((char) ch);
            }
    } else {
        while ((ch = dis.read()) != -1)
            messagebuffer.append((char) ch);
    }

    dis.close();
    return dumpHex(md.digest());
}

private static String dumpHex(byte[] data) {
    final int n = data.length;
    final StringBuilder sb = new StringBuilder(n * 3 - 1);
    for (int i = 0; i < n; i++) {
        if (i > 0) {
            sb.append(' ');
        }
        sb.append(HEX_CHARS[(data[i] >> 4) & 0x0F]);
        sb.append(HEX_CHARS[data[i] & 0x0F]);
    }
    return sb.toString();
} 
