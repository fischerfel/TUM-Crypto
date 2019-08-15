private SSLContext getSSLContext(){
      try{
          KeyStore keyStore = KeyStore.getInstance("PKCS12");
          InputStream in = context.getResources().openRawResource(R.raw.i);
          try {
              keyStore.load(in,p);

          } finally {
              in.close();
          }

          KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
          InputStream instream = context.getResources().openRawResource(R.raw.cert);
          byte[] der =loadPemCertificate(instream);
          ByteArrayInputStream derInputStream = new ByteArrayInputStream(der);
          CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
          X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
          String alias = cert.getSubjectX500Principal().getName();
          trustStore.load(null);
          trustStore.setCertificateEntry(alias, cert);
          instream.close();

          KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
          kmf.init(keyStore, pw);
          KeyManager[] keyManagers = kmf.getKeyManagers();

          TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
          tmf.init(trustStore);



          SSLContext sslContext = SSLContext.getInstance("TLSv1");
          sslContext.init(keyManagers, new TrustManager[]{mtm}, null);



          return sslContext;
      }catch (Exception e){


      }

      return null;
  }
