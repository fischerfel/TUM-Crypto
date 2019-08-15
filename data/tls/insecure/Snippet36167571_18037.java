 if (builder.sslSocketFactory != null || !isTLS) {
      this.sslSocketFactory = builder.sslSocketFactory;
    } else {
      try {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        this.sslSocketFactory = sslContext.getSocketFactory();
      } catch (GeneralSecurityException e) {
        **throw new AssertionError(); // The system has no TLS. Just give up.**
      }
    }
