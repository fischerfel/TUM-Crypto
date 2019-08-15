SSLContext.getInstance("TLS")
.init(null, trustAllCerts, new SecureRandom())
.getSupportedSSLParameters()
.getCipherSuites()
