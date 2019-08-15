SSLContext sslContext = SSLContext.getInstance("SSLv3");
sslContext.init(null, new TrustManager[] {new CustomX509TrustManager()}, new java.security.SecureRandom());
SSLSocketFactory ssf = sslContext.getSocketFactory();
HttpsURLConnection conn = (HttpsURLConnection) new URL("https://www.facebook.com/").openConnection();

        conn.setSSLSocketFactory(ssf);
        conn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(1000 * 2);
        conn.connect();
        //READ HTML
        conn.disconnect();
