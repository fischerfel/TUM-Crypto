public final void a(int paramInt1, int paramInt2)
  {
    this.b = paramInt2;
    InetSocketAddress localInetSocketAddress = new InetSocketAddress(InetAddress.getByName(this.a), this.b);
    while (true)
    {
      try
      {
        SSLContext localSSLContext = SSLContext.getInstance("TLS");
        X509TrustManager[] arrayOfX509TrustManager = new X509TrustManager[1];
        arrayOfX509TrustManager[0] = new c(this);
        localSSLContext.init(null, arrayOfX509TrustManager, new SecureRandom());
        this.e = ((SSLSocket)localSSLContext.getSocketFactory().createSocket());
        this.e.connect(localInetSocketAddress, paramInt1);
        this.d.clear();
        if (Arrays.asList(this.e.getSupportedProtocols()).contains("TLSv1.2"))
        {
          this.d.add("TLSv1.2");
          String[] arrayOfString1 = (String[])this.d.toArray(new String[this.d.size()]);
          SSLSocket localSSLSocket1 = this.e;
          if (arrayOfString1.length > 0)
            localSSLSocket1.setEnabledProtocols(arrayOfString1);
          this.c.clear();
          if (!Arrays.asList(this.e.getSupportedCipherSuites()).contains("TLS_RSA_WITH_AES_256_CBC_SHA"))
            break label374;
          this.c.add("TLS_RSA_WITH_AES_256_CBC_SHA");
          String[] arrayOfString2 = (String[])this.c.toArray(new String[this.c.size()]);
          SSLSocket localSSLSocket2 = this.e;
          if (arrayOfString2.length > 0)
            localSSLSocket2.setEnabledCipherSuites(arrayOfString2);
          e.a().a(this.e.getLocalAddress().getAddress());
          e.a().a(this.e.getLocalPort());
          a(5000);
          this.e.startHandshake();
          a(0);
          return;
        }
      }
      catch (Exception localException)
      {
        throw new IOException(localException.toString());
      }
      if (Arrays.asList(this.e.getSupportedProtocols()).contains("TLSv1.1"))
      {
        this.d.add("TLSv1.1");
      }
      else if (Arrays.asList(this.e.getSupportedProtocols()).contains("TLSv1"))
      {
        this.d.add("TLSv1");
        continue;
        label374: if (Arrays.asList(this.e.getSupportedCipherSuites()).contains("TLS_RSA_WITH_AES_128_CBC_SHA"))
          this.c.add("TLS_RSA_WITH_AES_128_CBC_SHA");
        else if (Arrays.asList(this.e.getSupportedCipherSuites()).contains("SSL_RSA_WITH_3DES_EDE_CBC_SHA"))
          this.c.add("SSL_RSA_WITH_3DES_EDE_CBC_SHA");
        else if (Arrays.asList(this.e.getSupportedCipherSuites()).contains("DES-CBC3-SHA"))
          this.c.add("DES-CBC3-SHA");
      }
    }
  }
