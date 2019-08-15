public class GetMethodEx {

public String getInternetData() throws Exception {


    BufferedReader in = null;
    String data = null;

    try {
        HttpClient client = new DefaultHttpClient();
        client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());

        URI website = new URI("https://server.com:8443/XoW"); 
        HttpGet request = new HttpGet();
        request.setURI(website);
        HttpResponse response = client.execute(request);
        response.getStatusLine().getStatusCode();

        in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String l = "";
        String nl = System.getProperty("line.separator");
        while ((l = in.readLine()) != null) {
            sb.append(l + nl);
        }
        in.close();
        data = sb.toString();
        return data;
    } finally {
        if (in != null) {
            try {
                in.close();
                return data;
            } catch (Exception e) {
                Log.e("GetMethodEx", e.getMessage());
            }
        }
    }
}

public Scheme getMockedScheme() throws Exception {
    MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory();
    return new Scheme("https", mySSLSocketFactory, 443);
}

class MySSLSocketFactory extends SSLSocketFactory {
    javax.net.ssl.SSLSocketFactory socketFactory = null;

    public MySSLSocketFactory(KeyStore truststore) throws Exception {
        super(truststore);
        socketFactory = getSSLSocketFactory();
    }

    public MySSLSocketFactory() throws Exception {
        this(null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
            UnknownHostException {
        return socketFactory.createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return socketFactory.createSocket();
    }

    javax.net.ssl.SSLSocketFactory getSSLSocketFactory() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sslContext.init(null, new TrustManager[] { tm }, null);
        return sslContext.getSocketFactory();
    }
}
}
