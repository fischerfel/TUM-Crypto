KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
trustStore.load(null, null);

MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

httpClient.setTimeout(30 * 1000);
httpClient.setSSLSocketFactory(socketFactory);
