public String getData(String URL)
{
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs,
                String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs,
                String authType) {
        }

    } };

    String output = "";
    try{

        //System.setProperty("https.proxyHost", "<PROXY HOST IP>");   // Uncomment if using proxy
        //System.setProperty("https.proxyPort", "<PROXY HOST PORT>");        // Uncomment if using proxy
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /*
         * end of the fix
         */

        URL url = new URL(URL);
        URLConnection con = url.openConnection();

        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);

        String inputLine;


        while ((inputLine = in.readLine()) != null) {
            output = output + inputLine;
        }

        System.out.println(output);
        in.close();

    }   
    catch(Exception e){
        e.printStackTrace();            
    }


    return output;
}
