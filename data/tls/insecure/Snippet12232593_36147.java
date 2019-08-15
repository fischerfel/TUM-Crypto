public static void downloadWeb() {
        // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

    // Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {}

            //begin download as regular http
        try {
            String wordAddress = "https://www.google.com/webhp?hl=en&tab=ww#hl=en&tbs=dfn:1&sa=X&ei=obxCUKm7Ic3GqAGvoYGIBQ&ved=0CDAQBSgA&q=pronunciation&spell=1&bav=on.2,or.r_gc.r_pw.r_cp.r_qf.&fp=c5bfe0fbd78a3271&biw=1024&bih=759";
            URLConnection yc = new URL(wordAddress).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println(wordAddress);
            }

        } catch (IOException e) {}

    }
