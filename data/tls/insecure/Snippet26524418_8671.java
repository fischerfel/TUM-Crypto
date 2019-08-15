SSLContext sslContext = null;
public HttpClientImpl() 
{


    try {
        sslContext = SSLContext.getInstance("TLS");
        setupTrustManager();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}


//set up a TrustManager
private void setupTrustManager()
{
     System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    try {
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            System.out.println("getAcceptedIssuers =============");
                            return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs,
                                    String authType) {
                            System.out.println("checkClientTrusted =============");
                    }

                    public void checkServerTrusted(X509Certificate[] certs,
                                    String authType) {
                            System.out.println("checkServerTrusted =============");
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] arg0,
                            String arg1)
                            throws java.security.cert.CertificateException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] arg0,
                            String arg1)
                            throws java.security.cert.CertificateException {
                        // TODO Auto-generated method stub

                    }
        } }, new SecureRandom());
    } catch (KeyManagementException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}

HttpParams httpParams = new BasicHttpParams();
HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeoutMillis);
HttpConnectionParams.setSoTimeout(httpParams, socketTimeoutMillis);  

// Use the above SSLContext to create your socket factory
 // Accept any hostname, so the self-signed certificates don't fail
   SSLSocketFactory sf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(new Scheme("https", sf, port)); 

HttpParams httpParams = new BasicHttpParams();
HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeoutMillis);
HttpConnectionParams.setSoTimeout(httpParams, socketTimeoutMillis);  

// Use the above SSLContext to create your socket factory
   SSLSocketFactory sf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
   // Accept any hostname, so the self-signed certificates don't fail


SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),port));
schemeRegistry.register(new Scheme("https", sf, port)); 
defaulthttpclient = new DefaultHttpClient(httpParams);
defaulthttpclient.setHttpRequestRetryHandler(myRetryHandler);       
UrlEncodedFormEntity query = new UrlEncodedFormEntity(paymentParams);

String func = "";            
fullurl = url+":"+Integer.toString(port)+"/"+request_uri;

HttpPost httppost = new HttpPost(fullurl);       
httppost.setEntity(query);
HttpResponse response_ = defaulthttpclient.execute(httppost);
