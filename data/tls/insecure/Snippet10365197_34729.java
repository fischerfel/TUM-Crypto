static X509TrustManager tm = new X509TrustManager() {

        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
        return null;
        }
};

MyConnectionManager(SchemeRegistry scheme){
    super(scheme);
}

public static MyConnectionManager getInstance() {
    if (instance == null){

        SSLContext ctx=null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }                   
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register( new Scheme("http", 80,PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        instance = new MyConnectionManager(schemeRegistry);
         // Increase max total connection to 200
         instance.setMaxTotal(15);
         // Increase default max connection per route to 20
         instance.setDefaultMaxPerRoute(15);
         // Increase max connections for localhost:80 to 50
         HttpHost localhost = new HttpHost("picasaweb.google.com", 443);
         instance.setMaxForRoute(new HttpRoute(localhost), 10);
    }
    return instance;
}
