private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
   public boolean verify(String hostname, SSLSession session) {
      return true;
   }
};
...
HttpsUrlConnection con = (HttpsUrlConnection)new URL("https://foo.bar.com").openConnection();
con.setHostnameVerifier(DO_NOT_VERIFY);
InputStream in = con.getInputStream();
