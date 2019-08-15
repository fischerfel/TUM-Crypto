DefaultHttpClient seed = new DefaultHttpClient();
SchemeRegistry registry = new SchemeRegistry();

SSLSocketFactory ssf = SSLSocketFactory.getSocketFactory();

// XXX: This verifier isn't working with Subject Alternative Names
ssf.setHostnameVerifier(new BrowserCompatHostnameVerifier());

registry.register(new Scheme("https", ssf, 443));

SingleClientConnManager mgr = new SingleClientConnManager(seed.getParams(), registry);
DefaultHttpClient http = new DefaultHttpClient(mgr, seed.getParams());

// Config point, change to your preference
String url = "https://mtvniph1-f.akamaihd.net/e3_ubisoft_prod0.m3u8";

HttpGet method = new HttpGet(url);

HttpResponse response = null;
try
{
    response = http.execute(method);
}
catch (Exception e)
{
    Log.e(TAG, "Request failed", e);
}
