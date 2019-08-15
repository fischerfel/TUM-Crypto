private static class CookiePersistingClient extends ApacheClient {

    private static final int HTTPS_PORT = 443;
    private static final int SOCKET_TIMEOUT = 300000;
    private static final int CONNECTION_TIMEOUT = 300000;

    public CookiePersistingClient() {
        super(createDefaultClient());
    }

    private static HttpClient createDefaultClient() {
        // Registering https clients.
        SSLSocketFactory sf = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", sf, HTTPS_PORT));
        // More customization (https / timeouts etc) can go here...

        ClientConnectionManager cm = new ThreadSafeClientConnManager(
                params, registry);
        DefaultHttpClient client = new DefaultHttpClient(cm, params);

        // Set the default cookie store
        client.setCookieStore(COOKIE_STORE);

        return client;
    }

    @Override
    protected HttpResponse execute(final HttpClient client,
            final HttpUriRequest request) throws IOException {
        // Set the http context's cookie storage
        BasicHttpContext mHttpContext = new BasicHttpContext();
        mHttpContext.setAttribute(ClientContext.COOKIE_STORE, COOKIE_STORE);
        return client.execute(request, mHttpContext);
    }

    @Override
    public Response execute(final Request request) throws IOException {
        Response response = super.execute(request);
        if (response.getStatus() == 401) {

            // Retrofit Callback to handle AccessToken
            Callback<AccessTockenResponse> accessTokenCallback = new Callback<AccessTockenResponse>() {

                @SuppressWarnings("deprecation")
                @Override
                public void success(
                        AccessTockenResponse loginEntityResponse,
                        Response response) {
                    try {
                        String accessToken =  loginEntityResponse
                                .getAccessToken();
                        TypedOutput body = request.getBody();
                        ByteArrayOutputStream byte1 = new ByteArrayOutputStream();
                        body.writeTo(byte1);
                        String s = byte1.toString();
                        FormUrlEncodedTypedOutput output = new FormUrlEncodedTypedOutput();
                        String[] pairs = s.split("&");
                        for (String pair : pairs) {
                            int idx = pair.indexOf("=");
                            if (URLDecoder.decode(pair.substring(0, idx))
                                    .equals("access_token")) {
                                output.addField("access_token",
                                        accessToken);
                            } else {
                                output.addField(URLDecoder.decode(
                                        pair.substring(0, idx), "UTF-8"),
                                        URLDecoder.decode(
                                                pair.substring(idx + 1),
                                                "UTF-8"));
                            }
                        }
                        execute(new Request(request.getMethod(),
                                request.getUrl(), request.getHeaders(),
                                output));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    // Handle Error while refreshing access_token
                }
            };
            // Call Your retrofit method to refresh ACCESS_TOKEN
            refreshAccessToken(GRANT_REFRESH,CLIENT_ID, CLIENT_SECRET_KEY,accessToken, accessTokenCallback);
        }

        return response;
    }
}
