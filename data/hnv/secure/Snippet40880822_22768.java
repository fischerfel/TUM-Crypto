private RequestQueue getRequestQueue() {
    HurlStack hurlStack = new HurlStack() {
        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
            try {
                httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpsURLConnection;
        }
    };

    if(mRequestQueue == null) {
        // this will make sure that this particular instance will last the lifetime of the app
        // getApplicationContext() is key, it keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), hurlStack);
    }
    return mRequestQueue;
}
