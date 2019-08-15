try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            LOGGER.debug("All Certificates Have Been Trusted Successfully.");
        } catch (KeyManagementException ex) {
            LOGGER.error("Error:",ex);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("Error:",ex);
        } 
