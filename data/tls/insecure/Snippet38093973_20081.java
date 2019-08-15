protected String doInBackground(String... arg0) {     
  try {
    ByteArrayInputStream derInputStream = new ByteArrayInputStream(app.certificateString.getBytes());
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509","BC");
    X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
    String alias = "alias";//cert.getSubjectX500Principal().getName();

    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null);
    trustStore.setCertificateEntry(alias, cert);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
    kmf.init(trustStore, null);
    KeyManager[] keyManagers = kmf.getKeyManagers();

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init(trustStore);
    TrustManager[] trustManagers = tmf.getTrustManagers();

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, trustManagers, null);
    URL url = new URL("MY HTTPS URL");
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setSSLSocketFactory(sslContext.getSocketFactory());

    // set Timeout and method
    conn.setReadTimeout(7000);
    conn.setConnectTimeout(7000);
    conn.setRequestMethod("POST");
    conn.setDoInput(true);

    // Add any data you wish to post here
    conn.connect();
    String reult = String.valueOf(conn.getInputStream());
    Log.d("connection : ", String.valueOf(reult));

  } catch (UnsupportedEncodingException e) {
    e.printStackTrace();
  } catch (IOException e) {
    e.printStackTrace();
  }  catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
  } catch (KeyManagementException e) {
    e.printStackTrace();
  } catch (CertificateException e) {
    e.printStackTrace();
  } catch (KeyStoreException e) {
    e.printStackTrace();
  } catch (NoSuchProviderException e) {
    e.printStackTrace();
  } catch (UnrecoverableKeyException e) {
    e.printStackTrace();
  }
  return null;
}
