            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return false;
            }

    });

    SSLContext context = null;
    if (trustManagers == null) {
            trustManagers = new TrustManager[] { new _FakeX509TrustManager() };
    }

    try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
    } catch (KeyManagementException e) {
            e.printStackTrace();
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
