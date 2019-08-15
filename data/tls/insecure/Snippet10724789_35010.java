    public UtilSSLSocketFactory trustAllCertificates() {
            if (trustingAllCertificates)
                    //Already doing this, no need to do it again
                    return this;
            trustingAllCertificates = true;
            try {
                    TrustManager[] tm = new TrustManager[]{new TrustingX509TrustManager()};
                    SSLContext context = SSLContext.getInstance("SSL");
                    context.init(new KeyManager[0], tm, new SecureRandom());
                    wrappedFactory = (SSLSocketFactory) context.getSocketFactory();
            } catch (Exception e) {
                    throw new RuntimeException("Can't recreate socket factory that trusts all certificates", e);
            }
            return this;
    }
