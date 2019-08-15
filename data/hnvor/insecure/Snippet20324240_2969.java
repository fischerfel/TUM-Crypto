 private void connect(){
            String alias = getAliasForClientCertificate();

            final X509Certificate[] certificates =getCertificateChain(alias);
            final PrivateKey pk = getPrivateKey(alias);



            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());


            X509ExtendedKeyManager keyManager = new X509ExtendedKeyManager() {

                @Override
                public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
                    return alias;
                }

                @Override
                public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
                    return alias;
                }

                @Override
                public X509Certificate[] getCertificateChain(String s) {
                    return certificates;
                }

                @Override
                public String[] getClientAliases(String s, Principal[] principals) {
                    return new String[]{alias};
                }

                @Override
                public String[] getServerAliases(String s, Principal[] principals) {
                    return new String[]{alias};
                }

                @Override
                public PrivateKey getPrivateKey(String s) {
                    return pk;
                }
            };

            TrustManagerFactory trustFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustFactory.init(trustStore);

            TrustManager[] trustManagers = trustFactory.getTrustManagers();



            X509TrustManager[] tm = new X509TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                //            public X509Certificate[] getAcceptedIssuers() {
                //                return certificates;
                //            }

                public X509Certificate[] getAcceptedIssuers() {
                    return certificates;
                }

                public boolean isClientTrusted(X509Certificate[] arg0) {
                    return true;
                }
            public boolean isServerTrusted(X509Certificate[] arg0) {
                return true; 
            }


        } };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(new KeyManager[] {keyManager}, tm, null);
        SSLContext.setDefault(sslContext);

        URL url = new URL("url..");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url
                .openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        urlConnection.setHostnameVerifier(hv);


        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();

}

private X509Certificate[] getCertificateChain(String alias) {
        try {
            return KeyChain.getCertificateChain(this, alias);
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
}
