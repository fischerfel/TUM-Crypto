public boolean authenticate() {
    try {
        System.setProperty("jsse.enableSNIExtension", "false");
        System.setProperty("com.sun.net.ssl.enableECC", "false");

        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);

        URL url = new URL("https://...");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setUseCaches(false);
        con.setInstanceFollowRedirects(true);
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        // KeyStore
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream("PATH/TO/.P12/file"), "P12password".toCharArray());
        keyManagerFactory.init(keyStore, "P12password".toCharArray());
        // ---

        // TrustStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("PATH/TO/.JKS/file"), "JKSpassword".toCharArray());
        trustManagerFactory.init(trustStore);
        // ---

        SSLContext context = SSLContext.getInstance("SSLv3");
        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        con.setSSLSocketFactory(context.getSocketFactory());

        con.getContent();
        CookieStore cookieJar =  manager.getCookieStore();
        List<HttpCookie> cookies = cookieJar.getCookies();
        for (HttpCookie cookie: cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                COOKIE_VALUE = cookie.getValue();
                return true;
            }
        }
        return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
