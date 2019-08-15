public static OkHttpClient getNewHttpClient() {

    CertificatePinner.Builder certPinnerBuilder = new CertificatePinner.Builder();

    for (Map.Entry<String, String> entry : THUMB_PRINTS.entrySet()) {
        certPinnerBuilder.add(entry.getKey(), entry.getValue());
    }

    CertificatePinner certPinner = certPinnerBuilder.build();

    OkHttpClient.Builder client = new OkHttpClient.Builder()
            .certificatePinner(certPinner)
            .addNetworkInterceptor(new AdditionalHeaderInterceptor())
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(true)
            .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .cache(null);

    return enableTls12OnPreLollipop(client).build();
}

public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
    if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
        try {
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, null, null);
            client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

            ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            List<ConnectionSpec> specs = new ArrayList<>();
            specs.add(cs);
            specs.add(ConnectionSpec.COMPATIBLE_TLS);
            specs.add(ConnectionSpec.CLEARTEXT);

            client.connectionSpecs(specs);
        } catch (Exception exc) {
            Log.e(Constants.Tags.GENERAL, "Error while setting TLS 1.2", exc);
        }
    }

    return client;
}
