ClientConnectionManager cm = new BasicClientConnectionManager();
cm.getSchemeRegistry().register(createHttpsScheme());
DefaultHttpClient client = new DefaultHttpClient(cm);
String url = "https://your domain/your url";
HttpGet get = new HttpGet(url);
HttpResponse resp = client.execute(get);

etc..

public static Scheme createHttpsScheme() {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[] {
                new TestTrustManager()
        }, new SecureRandom());

        SSLSocketFactory sf = new SSLSocketFactory(context);
        return new Scheme("https", 443, sf);
}
