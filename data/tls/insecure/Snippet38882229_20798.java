private void initializeSocketFactory() {
    if (Build.VERSION.SDK_INT >= 23) {
        HTTP_TRANSPORT = new NetHttpTransport();
    } else {
        //Android 5 and bellow needs this SSL Socket factory initialization
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(null, null, null);
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            NetHttpTransport.Builder netTransportBuilder = new NetHttpTransport.Builder();
            netTransportBuilder.setSslSocketFactory(socketFactory);
            HTTP_TRANSPORT = netTransportBuilder.build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            Log.e(LOG_TAG, "Problem instantiating cipher for ssl socket", e);
        }
    }

}
