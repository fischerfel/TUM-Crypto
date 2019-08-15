Naive custom TrustManager (empty checkServerTrusted)

SSLContext sslContext = SSLContext.getInstance("SSL");
TrustManager trustManagerNaive =    new X509TrustManager(){
        @Override
        public void checkClientTrusted(
                X509Certificate[] chain,
                String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void checkServerTrusted(
                X509Certificate[] chain,
                String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
        }
    };

sslContext.init(null, new TrustManager[]{trustManagerNaive}, null);

SSLSocketFactory socketFactory = (SSLSocketFactory)sslContext.getSocketFactory();
SSLSocket socket = (SSLSocket)socketFactory.createSocket(host, Integer.parseInt(port_number_et.getText().toString()));

//Native Android behavior (does not accept any untrusted certificate)
SSLSocketFactory socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
SSLSocket socket = (SSLSocket)socketFactory.createSocket(host, Integer.parseInt(port_number_et.getText().toString()));                      
