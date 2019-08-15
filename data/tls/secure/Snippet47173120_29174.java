String testURL = "https://api.chargeio.com/status";
SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
sslcontext.init(null, null, null);
try {
    SSLConnectionSocketFactory socketFactory = new
    SSLConnectionSocketFactory(sslcontext,
        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER); // Socket
    HttpClient client =
        HttpClients.custom().setSSLSocketFactory(socketFactory).build();
    HttpGet httpget = new HttpGet(testURL);
    HttpResponse response = client.execute(httpget);
    System.out.println(EntityUtils.toString(response.getEntity()));
    System.out.println("Response Code (Apache): " + response.getStatusLine().getStatusCode());
} catch (Exception e) {
    System.err.println("HttpsURLConnection Failed");
    e.printStackTrace();
}
