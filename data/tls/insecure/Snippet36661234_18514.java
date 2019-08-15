        sbinary.WSHttpBinding_IServiceStub stub;
        stub = (sbinary.WSHttpBinding_IServiceStub)new sbinary.ServiceLocator().getWSHttpBinding_IService();

        stub._setProperty(org.apache.axis.client.Call.CHECK_MUST_UNDERSTAND, Boolean.FALSE);
        stub._setProperty(Stub.USERNAME_PROPERTY, serviceUsername);
        stub._setProperty(Stub.PASSWORD_PROPERTY, servicePassword);
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        try {
            AxisProperties.setProperty("axis.socketSecureFactory","org.apache.axis.components.net.SunFakeTrustSocketFactory");
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ex) {
            // take action
        }
        addWsSecurityHeader(stub, serviceUsername, servicePassword);
        stub.pingDatabase();`
