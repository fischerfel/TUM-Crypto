SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        SSLParameters params = context.getSupportedSSLParameters();
        List<String> enabledCiphers = new ArrayList<String>();
        for (String cipher : params.getCipherSuites()) {
            boolean exclude = false;
            if (exludedCipherSuites != null) {
                for (int i = 0; i < exludedCipherSuites.length && !exclude; i++) {
                    exclude = cipher.indexOf(exludedCipherSuites[i]) >= 0;
                }
            }
            if (!exclude) {
                enabledCiphers.add(cipher);
            }
        }
        String[] cArray = new String[enabledCiphers.size()];



        SSLSocketFactory sf = context.getSocketFactory();
        sf = new DOSSLSocketFactory(sf, cArray);
        urlConnection.setSSLSocketFactory(sf);
