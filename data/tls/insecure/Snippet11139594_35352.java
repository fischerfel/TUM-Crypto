public static void allowAllSSL() {

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            connection = (HttpsURLConnection) (new URL(
                    "https://sPublic/Client.asmx"))
                    .openConnection();
            connection.connect();
        } catch (Exception e) {
            e.getMessage();
        }
