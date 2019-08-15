public WebResourceResponse shouldInterceptRequest(WebView view, String sourceurl) {
     try {

         URL url = new URL(sourceurl);

         HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

         //make https connection using tls
         SSLContext sslcontext = SSLContext.getInstance("TLS");
         sslcontext.init(null, null, null);
         SSLSocketFactory noSSLv3Factory = null;

         noSSLv3Factory = new TLSSocketFactory();
         urlConnection.setSSLSocketFactory(noSSLv3Factory);

         String responseEncoding = urlConnection.getContentEncoding();
         String responseMimeType = urlConnection.getContentType();
         return new WebResourceResponse(responseMimeType, responseEncoding, urlConnection.getInputStream());

    } catch (Exception e) {
          e.printStackTrace();
    }

    return null;
}
