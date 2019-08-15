// Http Client with SSL factory

public HttpClient getNewHttpClient() {
 try {
 KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
 trustStore.load(null, null);

 SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
 sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

 HttpParams params = new BasicHttpParams();
 HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
 HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

 SchemeRegistry registry = new SchemeRegistry();
 registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
 registry.register(new Scheme("https", sf, 443));

 ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

 return new DefaultHttpClient(ccm, params);
 } catch (Exception e) {
     return new DefaultHttpClient();
 }
}

// Post request for multi part entity

public void postRequest(){

    DefaultHttpClient httpClient = new getNewHttpClient();
    HttpPost postRequest = new HttpPost(url);
    String auth = "USER_NAME" + ":" + "PASSWORD";
    byte[] bytes = auth.getBytes();
    postRequest.setHeader("Authorization", "Basic " + new String(Base64.encodeBytes(bytes)));

    try {
        MultipartEntity mpC = new MultipartEntity();
        FileBody fb = new FileBody(message);
        StringBody sbPicID = new StringBody(fb.getFilename());
        mpC.addPart("name", sbPicID);
        mpC.addPart("file", fb);
        postRequest.setEntity(mpC);
        HttpResponse res;
        res = httpClient.execute(postRequest);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        String resPictureId = "";
        resPictureId = rd.readLine();

        Session.put("PICTURE_"+position, resPictureId);
        res.getEntity().getContent().close();
    }catch (Exception e) {
        // TODO: handle exception
    }

}

// SSL factory class

public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

}
