TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        URL url = new URL("--insert server url here--");
//url.openStream();
//  URLConnection urlConnection = url.openConnection();
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
} catch (MalformedURLException mfe) {
            mfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
