  try {

            String sslCertificateString= context.getResources().getString(R.string.certifiacte);
            InputStream certFile = new ByteArrayInputStream(sslCertificateString.getBytes(StandardCharsets.UTF_8));
            java.security.cert.Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(certFile);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            //create a factory
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            //get context
            SSLContext mySSLContext = SSLContext.getInstance("TLS");
            //init context
            mySSLContext.init(
                    null,
                    trustManagerFactory.getTrustManagers(),
                    new SecureRandom()
            );
            IO.Options opts = new IO.Options();
            opts.sslContext = mySSLContext;
            opts.secure = true;

            socket = IO.socket("https://demo.com:3000", opts); 
            socket.connect();


        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
