public static void main(String[] args) {
    try{
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustAllCerts, null);
        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();            
        HttpHost proxy = new HttpHost("127.0.0.1", 8888,"http");
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
        HttpGet request = new HttpGet("https://www.javaworld.com.tw/jute/post/view?bid=29&id=312144");
        CloseableHttpResponse response = client.execute(request);
        String entity = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(entity);
    }catch (Exception e) {
        e.printStackTrace();
    }
}
