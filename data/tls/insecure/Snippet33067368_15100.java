OkHttpClient client = new OkHttpClient();
        try{
            client.setSslSocketFactory(getPinnedCertSslSocketFactory(context));
            RequestBody formBody = new FormEncodingBuilder()
                    .add("params", "xxx")
                    .build();
            Request request = new Request.Builder()
                    .url("https:\\mydomain.com")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


private SSLSocketFactory getPinnedCertSslSocketFactory(Context context) {
    try {
        KeyStore trusted = KeyStore.getInstance("BKS");
        InputStream in = context.getResources().openRawResource(R.raw.mycert);
        trusted.load(in, "mypass".toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trusted);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    } catch (Exception e) {
        Log.e("MyApp", e.getMessage(), e);
    }
    return null;
}
