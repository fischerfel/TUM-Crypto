SSLContext sslContext = null;
try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,MemorizingTrustManager.getInstanceList(getApplicationContext()), new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setCustomSSLContext(sslContext);
