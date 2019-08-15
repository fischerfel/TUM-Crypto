if (cpUrl.getProtocol().equals("https")) {
                        out.println("https", 0);
                        HttpsURLConnection connection = (HttpsURLConnection) cpUrl.openConnection();

                        TrustManager[] trustAllCerts = new TrustManager[] { new  BusinessIntelligenceX509TrustManager() };
                        SSLContext sc;

                        try {
                            sc = SSLContext.getInstance("SSL");
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            return;
                        }

                        HostnameVerifier hv = new BusinessIntelligenceHostnameVerifier();

                        try {
                            sc.init(null, trustAllCerts, new java.security.SecureRandom());
                        }
                        catch (KeyManagementException keyManagementException) {

                            return;
                        }

                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                        HttpsURLConnection.setDefaultHostnameVerifier(hv);

                        connection.setDoInput(true);
                        connection.setRequestProperty("Authorization", "Basic " + encode);

                        connection.setRequestMethod("POST");
                        connection.connect();
                        stream = connection.getInputStream();
                        Properties properties = new Properties();
                        properties.load(stream);

                    }
