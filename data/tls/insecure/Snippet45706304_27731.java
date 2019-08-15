Responsecallback responsecallback;

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    testing(mBase_Url);
}

public void testing(String urls) {

    String result = "";
    try {
        URL url = new URL(urls);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(getSSLCertificate()); // Tell the URLConnection to use a SocketFactory from our SSLContext
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("country", "IN");
        String query = builder.build().getEncodedQuery();

        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.println(query);
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); //,8192
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result = result.concat(inputLine);
        }

        responsecallback.displayResponse(result);
        in.close();
    } catch (IOException e) {
        result = e.toString();
        Log.e(TAG, "HTTP Error Result=" + result);
        responsecallback.displayResponse(result);
    }
}

private SSLSocketFactory getSSLCertificate() {
    try {
        // Get an instance of the Bouncy Castle KeyStore format
        KeyStore trusted = KeyStore.getInstance("PKCS12");
        // your trusted certificates (root and any intermediate certs)
        InputStream in = getResources().openRawResource(R.raw.xxxxxx); //SSL Certificate - P12 formate
        String password = "XXXXXXX"; // Certificate password

        char[] pwd = password.toCharArray();
        try {
            trusted.load(in, pwd);
        } finally {
            in.close();
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        kmf.init(trusted, pwd);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(trusted);

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(kmf.getKeyManagers(), getWrappedTrustManagers(), new SecureRandom());

        return context.getSocketFactory();

    } catch (Exception e) {
        Log.e(TAG, "Exception e=" + e.toString());
        throw new AssertionError(e);
    }
}

private TrustManager[] getWrappedTrustManagers() {
    return new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
                        throws java.security.cert.CertificateException {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                    return null;
                }
            }
    };
}
