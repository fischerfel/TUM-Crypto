    context.init(new KeyManager[] {}, new TrustManager[] {new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {return null; }

        @Override    
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}

    }}, new SecureRandom());
