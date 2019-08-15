    BufferedInputStream serverCertificateFile = new BufferedInputStream(new FileInputStream("somepath/server-cert.der"));
    X509Certificate serverCertificate = (X509Certificate)
              CertificateFactory.getInstance("X.509").generateCertificate(serverCertificateFile);
            sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0,
                            String arg1) throws CertificateException {
                        throw new CertificateException();
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0,
                            String arg1) throws CertificateException {
                        boolean valid = false;
                        for (X509Certificate certificate : arg0) {
                            try {
                                certificate.verify(serverCertificate.getPublicKey());
                                valid = true;
                                break;
                            } catch (SignatureException e) {}
                        }
                        if (!valid) {
                            throw new CertificateException();
                        }
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
            }, new SecureRandom());
