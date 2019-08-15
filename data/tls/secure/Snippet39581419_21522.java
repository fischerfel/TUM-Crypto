        try (FileInputStream truststoreFile = new FileInputStream("path/to/your/truststore.jks")) {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] trustorePassword = "<truststorePassword".toCharArray();
            truststore.load(truststoreFile, trustorePassword);
            trustManagerFactory.init(truststore);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            KeyManager[] keyManagers = {};//if you have key managers;

            sslContext.init(keyManagers, trustManagerFactory.getTrustManagers(), new SecureRandom());

            URL httpsUrl = new URL("<your https url>");
            URLConnection urlConnection = httpsUrl.openConnection();

        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
            //handle exception
        } catch (KeyManagementException e) {
           //handle exception
        }
