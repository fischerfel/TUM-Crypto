        OkHttpClient okHttpClient = new OkHttpClient();

        HostnameVerifier hostNameVerifier = new X509HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                try {
                    verifyHost(hostname);
                    return true;
                } catch (SSLException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                verifyHost(host);
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
                verifyHost(host);
            }

            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
                verifyHost(host);
            }

            private void verifyHost(String sourceHost) throws SSLException {
                if (!hostName.equals(sourceHost)) { // THIS IS WHERE YOU AUTHENTICATE YOUR EXPECTED host (IN THIS CASE 192.168.0.56)
                    throw new SSLException("Hostname '192.168.0.56' was not verified");
                }
            }
        };

        okHttpClient.setHostnameVerifier(hostNameVerifier);             
        OkClient okClient = new OkClient(okHttpClient);

         RestAdapter restAdapter = new RestAdapter.Builder()
        **.setClient(okClient)** //this is where u bind the httpClient
        .build(); //make sure you specify endpoint, headerInterceptor etc ...
