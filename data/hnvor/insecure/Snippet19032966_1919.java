URL url = new URL("https://www.xyz.com");
HttpsURLConnection httpURLConnection  = (HttpsURLConnection) url.openConnection();
httpURLConnection.setRequestProperty("Content-Type",
                "text/plain");
httpURLConnection.setRequestMethod("POST");
httpURLConnection.setDoOutput(true);
httpURLConnection.setAllowUserInteraction(false);
httpURLConnection.setInstanceFollowRedirects(true);
httpURLConnection.setHostnameVerifier(DO_NOT_VERIFY);
httpURLConnection.connect();
OutputStream outputStream = httpURLConnection.getOutputStream();
 outStream.write(datainbytes);



 final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
 };
