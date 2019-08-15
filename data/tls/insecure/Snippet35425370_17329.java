public static String simplePost(String myurl) {
        HttpsURLConnection conn = null;
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1");

            sslcontext.init(null, null, null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);


            StringBuffer response;
            URL url = new URL(myurl);
            conn = (HttpsURLConnection) url.openConnection();
            // conn.setReadTimeout(90000);
            // conn.setConnectTimeout(900000);
            conn.setRequestProperty("Content-Type", "application/json");

            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(new URL(myurl).getHost());

            conn.setDoOutput(true);
            conn.setRequestProperty("Cookie", cookie);


            conn.setRequestMethod("POST");

            int responseCode = conn.getResponseCode();
            switch (responseCode) {
                case 200:
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                default:
                    return "server error";
            }
        } catch (IOException | java.security.KeyManagementException | java.security.NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "exception";
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "exception";
                }
            }
        }
    }
