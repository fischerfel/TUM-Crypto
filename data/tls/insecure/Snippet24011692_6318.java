        try {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[] { new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                } }, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    } catch (Exception e) { // should never happen
        e.printStackTrace();
    }

    try {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.xxxx.xxxx", xxxx));
        URL url = new URL("https://api.forecast.io/forecast/xxxxxxxxx/12.9667,77.5667");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection(proxy);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
        conn.disconnect();
    } catch (MalformedURLException e) {

        e.printStackTrace();

    } catch (IOException e) {

        e.printStackTrace();

    }
return "";
