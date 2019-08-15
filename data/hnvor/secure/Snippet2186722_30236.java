HostnameVerifier ver = new HostnameVerifier()
{
  public boolean verify(String urlHostname,String certHostname)
  {
    return java.net.InetAddress.getByName(urlHostname).equals(java.net.InetAddress.getByName(certHostname));
  }
};
com.sun.net.ssl.HttpsURLConnection con = ...(obtain connection);
con.setHostnameVerifier(ver);
