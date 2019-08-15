if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP)
{
  sslContext = SSLContext.getInstance("TLS");
  sslContext.init(keyManagers, null, null);
  ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslContext.getSocketFactory());
}
else
{
  sslSocketFactoryEx = new SSLSocketFactoryEx(keyManagers, null, null);
  ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sslSocketFactoryEx);
}
