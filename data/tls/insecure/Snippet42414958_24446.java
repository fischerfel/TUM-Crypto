public  HttpsURLConnection setUpHttpsConnection()
{
    String HttpMessage="";
    int HttpResult=0;
    HttpsURLConnection urlConnection=null;
    try
    {
        Log.i("status","inside method..");

        InputStream caInput = getAssets().open("myapp_key.p12");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String pfxPassword = "test123"; // change it to the correct password
        keyStore.load(caInput, pfxPassword.toCharArray());
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] trustManagers = tmf.getTrustManagers();
        final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

        TrustManager[] wrappedTrustManagers = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return origTrustmanager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        origTrustmanager.checkClientTrusted(certs, authType);
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        try {
                            origTrustmanager.checkServerTrusted(certs, authType);
                        } catch (CertificateExpiredException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, wrappedTrustManagers, null);

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        urlConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        URL url = new URL("https://sandbox.api.visa.com/visadirect/mvisa/v1/merchantpushpayments/");
        urlConnection =(HttpsURLConnection)url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Authorization", "SYZK9LIO98QIQNQ27H6921fgRyt63FHIxrQP76m0hNYT6BZ7I:pB1g5XX3Hw58buPENR03ZM4Vgm7P");
        InputStream in = urlConnection.getInputStream();
       BufferedReader  reader = new BufferedReader(new InputStreamReader(in));
        String res = reader.toString();
        System.out.println(res);




    }catch(SSLHandshakeException he){
        he.printStackTrace();
    }
    catch (Exception ex)
    {
        Log.e("error", "Failed to establish SSL connection to server: " + ex.toString());
        return null;
    }
    return urlConnection;
}
