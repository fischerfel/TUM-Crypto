HttpURLConnection conn = (HttpURLConnection) req.url().openConnection();

conn.setRequestMethod(req.method().name());
conn.setInstanceFollowRedirects(false);
conn.setConnectTimeout(req.timeout());
conn.setReadTimeout(req.timeout());

if (conn instanceof HttpsURLConnection) {
    if (!req.validateTLSCertificates()) {
         initUnSecureTSL();
         ((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
         ((HttpsURLConnection)conn).setHostnameVerifier(getInsecureVerifier());
    }
}

if (req.method().hasBody())
    conn.setDoOutput(true);
if (req.cookies().size() > 0)
    conn.addRequestProperty("Cookie", getRequestCookieString(req));
for (Map.Entry<String, String> header : req.headers().entrySet()) {
    conn.addRequestProperty(header.getKey(), header.getValue());
}
