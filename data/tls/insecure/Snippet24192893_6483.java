public class HttpsClient{

    public static void main(String[] args)
    {
        new HttpsClient().testIt();
    }

    private void testIt(){

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            ;
        }

        String https_url = "https://api.kraken.com/0/public/Time";
        URL url;
        HttpsURLConnection con = null;
        try {

            url = new URL(https_url);
            con = (HttpsURLConnection)url.openConnection();

            //dumpl all cert info
            print_https_cert(con);

            try{
                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        @Override
        public X509Certificate[] getAcceptedIssuers(){return null;}
        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
    }};

}
