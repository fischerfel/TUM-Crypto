public class sampleMain {
                public static void main(String[] args) {

            ClientConfig clientConfig = new DefaultClientConfig();
            /*  clientConfig.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);*/
            clientConfig.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, 300000);
            clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 30000);

            disableSslVerification(clientConfig);
            Client client = Client.create(clientConfig);
            client.setFollowRedirects(true);
            HTTPBasicAuthFilter authenticationFilter = new HTTPBasicAuthFilter("admin", "APP@#1234");

            client.addFilter(authenticationFilter);
            WebResource webResource = client.resource("https://someIP:443/rest/api/topology/servicesnodes");
            ClientResponse response =  webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            System.out.println(response);
        }

//below method is used to set ssl context to clientconfig as https property
        private static void disableSslVerification(ClientConfig clientConfig) {
            try {
                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
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
                } };

                // Install the all-trusting trust manager
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                }, sc));

            } catch (NoSuchAlgorithmException ae1) {
            } catch (KeyManagementException e) {
            }
        }


    }
