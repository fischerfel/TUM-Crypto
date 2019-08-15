final SSLContext sslCtx = SSLContext.getInstance("TLSv1.2");
sslCtx.init(null, new TrustManager[] { new CustomX509TrustManager() }, new SecureRandom());

stub._getServiceClient().getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, new Protocol("https",
            (ProtocolSocketFactory) new SSLProtocolSocketFactory(sslCtx), 443));
