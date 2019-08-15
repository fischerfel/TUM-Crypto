    public MyOutBoundClientWSImpl(URL wsdlUrl){
        super(wsdlUrl, serviceName);
        this.wsUrl=wsdlUrl;
        this.mService = this.getMySoapHttpPort();
        Map<String, Object> requestContext = ((BindingProvider)mService).getRequestContext();
        requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, REQUEST_TIMEOUT); // Timeout in millis
        requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT); // Timeout in millis
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            ServerSocketFactory sf = SSLServerSocketFactory.getDefault();
            KeyManager[] km = ??;
            TrustManager[] tm = ??;
            SecureRandom random = ??;
            sslContext.init(km, tm, random);
            requestContext.put(BindingProviderProperties.SSL_SOCKET_FACTORY, sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
