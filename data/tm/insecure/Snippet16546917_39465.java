private static class DummyTrustManager implements X509TrustManager
    {
        private X509Certificate[] mCerts;

        public DummyTrustManager(Certificate[] pCerts)
        {
            // convert into x509 array
            mCerts = new X509Certificate[pCerts.length];
            for(int i = 0; i < pCerts.length; i++)
            {
                mCerts[i] = (X509Certificate)pCerts[i];
            }
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{}

        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return mCerts;
            //return new X509Certificate[0];
        }
    }
