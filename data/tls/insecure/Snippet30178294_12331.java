  private void doCustomSocketFactoryWithHttpUrlConnection() {

    try {
        String uri = "https://alice.sni.velox.ch";

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);
        CustomSocketFactory customSocketFactory = new CustomSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultSSLSocketFactory(customSocketFactory);

        URL url = new URL(uri);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        Log.d(TAG, "HTTP Response Code: " + conn.getResponseCode());

    } catch (Exception e) {
        Log.d(TAG, e.getLocalizedMessage());
    }

}
