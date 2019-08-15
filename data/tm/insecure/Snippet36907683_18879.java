<h1>I used below code to connect site minder authentication. Its working fine.</h1>

<code>
@SuppressWarnings("restriction")
    public static void main(String[] args) throws IOException
    {
        disableSslVerification();

        CookieManager cookimngr = new CookieManager(null, CookiePolicy.ACCEPT_ALL); 
        CookieHandler.setDefault(cookimngr);
        URL svrUrl = new URL("https://ABC_HOST:ABC_PORT/XYZ/");
        HttpURLConnection conn = (HttpURLConnection)svrUrl.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + new sun.misc.BASE64Encoder().encode((username+":"+password).getBytes()));

        InputStream in = conn.getInputStream();

         for (HttpCookie h : cookimngr.getCookieStore().getCookies()){
             System.out.println(h.getName());
             System.out.println(h.getValue());
         }
        in.close();

    }
/** disable the SSL Certificate Verification **/
    public static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }
</code>
