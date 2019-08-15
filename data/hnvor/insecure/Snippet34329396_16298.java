private HttpsURLConnection urlConnection;
private CookieManager cookieManager;

private HttpsURLConnection getConnection(String url) throws MalformedURLException {
    URL request_url = new URL(url);
    try {
        if (!isHttps()) {
            throw new ConnectException("you have to use SSL certifacated url!");
        }
        urlConnection = (HttpsURLConnection) request_url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(95 * 1000);
        urlConnection.setConnectTimeout(95 * 1000);
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("X-Environment", "android");

        /** Cookie Sets... */
        String cookie = cookieManager.getCookie(urlConnection.getURL().toString());
        cookieManager = CookieManager.getInstance();
        if (cookie != null)
            urlConnection.setRequestProperty("Cookie", cookie);

        List<String> cookieList = urlConnection.getHeaderFields().get("Set-Cookie");
        if (cookieList != null) {
            for (String cookieTemp : cookieList) {
                cookieManager.setCookie(urlConnection.getURL().toString(), cookieTemp);
            }
        }
        /** Cookie Sets... */

        urlConnection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                /** if it necessarry get url verfication */
                //return HttpsURLConnection.getDefaultHostnameVerifier().verify("your_domain.com", session);
                return true;
            }
        });
        urlConnection.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());


        urlConnection.connect();

    } catch (IOException e) {
        e.printStackTrace();
    }

    return urlConnection;
}
