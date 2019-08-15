public class DocumentApprover {

    static final String user = "user"; // your account name
    static final String pass = "password"; // your password for the account

    public static DefaultHttpClient wrapClient(DefaultHttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {

            DefaultHttpClient httpclient = wrapClient(new DefaultHttpClient());
            try {
                httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope("host", 443),
                    new NTCredentials(user, pass, "workstation", "domain"));

                HttpGet httpget = new HttpGet("url");

                System.out.println("executing request" + httpget.getRequestLine());
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
                }
                EntityUtils.consume(entity);
            } finally {
                // When HttpClient instance is no longer needed,
                // shut down the connection manager to ensure
                // immediate deallocation of all system resources
                httpclient.getConnectionManager().shutdown();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
