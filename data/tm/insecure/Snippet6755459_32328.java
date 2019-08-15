TrustManager tm = new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] chain,
                    String authType)
                    throws CertificateException {
        //do nothing, you're the client
    }

    public X509Certificate[] getAcceptedIssuers() {
        //also only relevant for servers
    }

    public void checkServerTrusted(X509Certificate[] chain,
                    String authType)
                    throws CertificateException {
        /* chain[chain.length -1] is the candidate for the
         * root certificate. 
         * Look it up to see whether it's in your list.
         * If not, ask the user for permission to add it.
         * If not granted, reject.
         * Validate the chain using CertPathValidator and 
         * your list of trusted roots.
         */
    }
};
