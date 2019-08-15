static SSLSocketFactory getSSLSocketFactory() {
        try {
            String keystoreFilename = "D:/Oracle/IDCS/MFA/APNS/OMA_prereqs/iOS_prod.p12";
            char[] storepass = "welcome1".toCharArray();
            FileInputStream fis = new FileInputStream(
                    new File(keystoreFilename));

            final KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, storepass);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance("SunX509");
            keyManagerFactory.init(ks, storepass);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance("SunX509");
            trustManagerFactory.init((KeyStore) null);
            // trustManagerFactory.init(ks);

            // create ssl context
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");


            // setup the HTTPS context and parameters
            sslContext.init(keyManagerFactory.getKeyManagers(),
                    trustManagerFactory.getTrustManagers(), null);

            if (keyManagerFactory != null || trustManagerFactory != null) {
                return SSLTunnelSocketFactory.wrap(proxy,
                        sslContext.getSocketFactory());
            }
        } catch (Exception e) {
            System.out.println("Unable to create ssl socket factory");
            e.printStackTrace();
        }
        return HttpsURLConnection.getDefaultSSLSocketFactory();
    }  

    private static void sendSamplePushNotification() {
                System.setProperty("javax.net.debug", "all");
                URL url = null;
                try {
                    HostnameVerifier hv = new HostnameVerifier() {
                        public boolean verify(String urlHostName, SSLSession session) {
                            return true;
                        }
                    };
                    JSONObject payload = new JSONObject();
                    JSONObject msg = new JSONObject();
                    try {
                        msg.put("alert", "Hello APNS");
                        payload.put("aps", msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    url = new URL(
                            "https://api.development.push.apple.com:443/3/device/00fc13adff785122b4ad28809a3420982341241421348097878e577c991de8f0");
                    HttpsURLConnection conn = (HttpsURLConnection) url
                            .openConnection(proxy);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setHostnameVerifier(hv);
                    conn.setSSLSocketFactory(getSSLSocketFactory());
                    /*conn.setRequestProperty("content-length", new String(payload
                            .toString().getBytes()));*/
                    conn.setConnectTimeout(3000);
                    conn.connect();
                    OutputStream os = conn.getOutputStream();
                    OutputStreamWriter writer = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(writer);
                    bw.write(payload.toString());
                    bw.flush();
                    //System.out.println("Response code : " + conn.getResponseCode());
                    System.out.println(conn.getResponseMessage());
                    InputStream is = conn.getInputStream();
                    ................
            }
