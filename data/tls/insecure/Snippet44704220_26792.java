SSLContext context = SSLContext.getInstance("TLSv1", BouncyCastleJsseProvider.PROVIDER_NAME);
System.out.println(Arrays.toString(context.getSupportedSSLParameters().getProtocols())); // [TLSv1.1, TLSv1, TLSv1.2]
