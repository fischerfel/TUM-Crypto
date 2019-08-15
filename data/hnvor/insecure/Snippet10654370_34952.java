private static String downloadString(final URL url) throws IOException {
    final HttpsURLConnection conn = (HttpsURLConnection) url
            .openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {

        @Override
        public boolean verify(final String hostname,
                final SSLSession session) {
            return true;
        }
    });
    conn.setReadTimeout(10000);

    String html;

    try {
        final InputStream is = conn.getInputStream();
        final BufferedInputStream bis = new BufferedInputStream(is);
        final ByteArrayBuffer baf = new ByteArrayBuffer(50);

        int current = 0;
        while ((current = bis.read()) != -1) {
            baf.append((byte) current);
        }

        /* Convert the Bytes read to a String. */
        html = new String(baf.toByteArray());

    } finally {
        conn.disconnect();
    }

    return html;
}
