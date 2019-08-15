public static OkHttpClient getUnsafeOkHttpClient(final X509TrustManager tm) {

    try{
        //Create a trust manager taht does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try{
                    tm.checkClientTrusted(chain, authType);
                }catch (CertificateException ce){}
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException{

                if(chain == null || chain.length == 0)throw new IllegalArgumentException("certificate is null or empty");
                if(authType == null || authType.length() == 0) throw new IllegalArgumentException("authtype is null or empty");
                if(!authType.equalsIgnoreCase("RSA"))throw new CertificateException("certificate is not trust");
                tm.checkServerTrusted(chain,authType);
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return tm.getAcceptedIssuers();
            }
        }};
