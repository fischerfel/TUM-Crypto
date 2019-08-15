if (getSharedPreferences("guest_token", MODE_PRIVATE).getString("gtkn",null) == null){
        AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        client.addHeader("Authorization", "myapikey");

        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.get("https://dev.sarshomar.com/api/v1/token/guest", new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject m_obj = new JSONObject(responseString);
                    String guest_token = m_obj.getJSONObject("msg").getJSONObject("callback").getString("token");

                    SharedPreferences pref = getSharedPreferences("guest_token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("gtkn", guest_token);
                    editor.apply();

                    Log.d("onSuccess_token: ", guest_token);
                    Toast.makeText(getApplicationContext(),guest_token,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorresponse, Throwable throwable) {
                throwable.printStackTrace();
                Log.d("onFailure: ",throwable.toString()+errorresponse+statusCode+throwable.getLocalizedMessage());
            }

            @Override
            public void onStart() {
                // called before request is started
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}

public SSLContext getSslContext() {

    TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    } };

    SSLContext sslContext=null;

    try {
        sslContext = SSLContext.getInstance("TLS");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    return sslContext;
}
