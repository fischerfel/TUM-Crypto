private TcpLink createSSL() {
        KeyStore keyStore = null;
        TrustManagerFactory tmf = null;
        SSLContext ctx = null;
        SSLSocket socket = null;
        TcpLink smscLink = null;

        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            LOGGER.info("Got keystore");
            keyStore.load(new FileInputStream("/root/keystore.jks"), "test".toCharArray());
            LOGGER.info("Loaded keystore");
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            LOGGER.info("Inited keystore");
            ctx = SSLContext.getInstance("TLSv1");
            ctx.init(null, tmf.getTrustManagers(), null);
            SSLSocketFactory factory = ctx.getSocketFactory();
            socket = (SSLSocket)factory.createSocket("100.100.201.189", 8807);
            LOGGER.info("Got socket");
            smscLink = new TcpLink(socket);

            return smscLink;

        } catch (KeyStoreException e) {
            LOGGER.error("Key store exception : " + e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("NoSuchAlgorithmException : " + e);
        } catch (CertificateException e) {
            LOGGER.error("CertificateException : " + e);
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException : " + e);
        } catch (IOException e) {
            LOGGER.error("FileNotFoundException : " + e);
        } catch (KeyManagementException e) {
            LOGGER.error("KeyManagementException : " + e);
        } catch (Exception e) {
            LOGGER.error("Exception : " + e);
        }
        return null;
    }
