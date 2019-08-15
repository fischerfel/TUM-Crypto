@Override   public WebResourceResponse shouldInterceptRequest(WebView
    view, WebResourceRequest request) {

    HttpsURLConnection httpsConnection =
    (HttpsURLConnection)urlConnection;

    httpsConnection.setHostnameVerifier(new PortalHostnameVerifider());

    SSLSocketFactory sslSocketFactory = this.getSSLContext().getSocketFactory();
    httpsConnection.setSSLSocketFactory(sslSocketFactory);
    httpsConnection.setRequestMethod(request.getMethod());

    String contentType = urlConnection.getContentType();

    }
