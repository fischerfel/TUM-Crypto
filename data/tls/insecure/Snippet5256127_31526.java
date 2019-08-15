try {
                URL u = new URL(url);



                HttpURLConnection http = null; 

                if (u.getProtocol().toLowerCase().equals("https")) { 

                    trustAllHosts(); 
                        HttpsURLConnection https = (HttpsURLConnection) u.openConnection(); 
                        https.setHostnameVerifier(DO_NOT_VERIFY); 
                        http = https; 
                        Log.v("log_tag", "https");
                } else { 
                    Log.v("log_tag", "http");
                        http = (HttpURLConnection) u.openConnection(); 
                } 
                http.setDoOutput(true);
                http.connect();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                Log.v("log_tag", "PATH: " + PATH);
                File file = new File(PATH);
                file.mkdirs();
                String fileName = "demo.pdf";
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                Log.d("log_tag", "fileoutput");

                InputStream in = http.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                in.close();
            } catch (IOException e) {
                Log.d("log_tag", "Error: " + e);
            }
private static void trustAllHosts() {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }



        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
            // TODO Auto-generated method stub

        }`} };
    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};
}
