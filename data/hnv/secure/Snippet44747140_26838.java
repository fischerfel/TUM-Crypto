/**
 * Opens HTTP/HTTPS and setup connection to given URL.
 *
 * @return prepared HttpURLConnection connection
 * @throws IOException in case URL is malformed or connection cannot be established
 */
public void openHttpUrlConnectionForGet() throws IOException {

  // Set Https protocols
  System.getProperties().setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3");

  // Create connection
  URL urlObject = new URL(location);
  HttpURLConnection conn;
  if (proxy != null) {
    InetSocketAddress adr = new InetSocketAddress(proxy.getPk().getAddress(), proxy.getPk().getPort());
    java.net.Proxy prx = new java.net.Proxy(java.net.Proxy.Type.HTTP, adr);
    conn = (HttpURLConnection) urlObject.openConnection(prx);
  } else {
    conn = (HttpURLConnection) urlObject.openConnection();
  }
  conn.setRequestMethod("GET");
  conn.setInstanceFollowRedirects(false);

  // Setup SSL factory
  if (conn instanceof HttpsURLConnection) {
    HttpsURLConnection httpsc = (HttpsURLConnection) conn;
    if (hostnameVerifier != null) {
      httpsc.setHostnameVerifier(hostnameVerifier);
    }
    if (trustManager != null) {
      try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
        httpsc.setSSLSocketFactory(sc.getSocketFactory());
      } catch (KeyManagementException | NoSuchAlgorithmException e) {
        throw new RuntimeException("Cannot init HTTPS connection.", e);
      }
    }
  }

  // Configure timeouts
  conn.setConnectTimeout(connectTimeout);
  conn.setDefaultUseCaches(false);
  conn.setUseCaches(false);
  conn.setDoOutput(false);
  conn.setReadTimeout(readTimeout);

  // Check connection
  if (StringUtils.isEmpty(userAgent)) {
    throw new IllegalArgumentException("Cannot create HTTP(S) connection. User-Agent header is not set.");
  }
  if (StringUtils.isEmpty(accept)) {
    throw new IllegalArgumentException("Cannot create HTTP(S) connection. Accept header is not set.");
  }
  if (StringUtils.isEmpty(acceptLanguage)) {
    throw new IllegalArgumentException("Cannot create HTTP(S) connection. Accept-Language header is not set.");
  }
  if (StringUtils.isEmpty(acceptEncoding)) {
    throw new IllegalArgumentException("Cannot create HTTP(S) connection. Accept-Encoding header is not set.");
  }

  // Set headers
  conn.setRequestProperty("User-Agent", userAgent);
  conn.setRequestProperty("Accept", accept);
  conn.setRequestProperty("Accept-Language", acceptLanguage);
  conn.setRequestProperty("Accept-Encoding", acceptEncoding);
  conn.setRequestProperty("Connection", "keep-alive");
  conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
  if (cookies != null) {
    conn.setRequestProperty("Cookie", cookies);
  }
  this.connection = conn;
}
