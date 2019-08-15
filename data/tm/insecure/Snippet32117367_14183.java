public Picasso getPicassoDownloader() throws NoSuchAlgorithmException, KeyManagementException
{


    TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}


                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] myTrustedAnchors = new X509Certificate[0];  
                    return myTrustedAnchors;
                }
            }
        };

     SSLContext sc = SSLContext.getInstance("SSL");
     sc.init(null, trustAllCerts, new SecureRandom());


    Picasso.Builder builder = new Picasso.Builder(getContext()).listener(new Listener() {

        @Override
        public void onImageLoadFailed(Picasso arg0, Uri arg1, Exception ex) {
            ex.printStackTrace();

        }
    });

    OkHttpClient client = new OkHttpClient();
    client.setSslSocketFactory(sc.getSocketFactory());
    client.setHostnameVerifier(new HostnameVerifier() {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    });

    client.networkInterceptors().add(new BasicAuthInterceptor());

    Downloader downloader = new OkHttpDownloader(client);

    return builder.downloader(downloader).build();
}
