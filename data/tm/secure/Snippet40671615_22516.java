I modified it as :

context.init(keyFactory.getKeyManagers(), new TrustManager[] { tm }, null);
TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    for (int j=0; j<chain.length; j++)
                    {
                        chain[j].checkValidity();
                        try {
                            chain[j].verify(ca.getPublicKey());
                        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException |
                                SignatureException e) {
                            e.printStackTrace();
                            throw new CertificateException(e.getMessage());
                        }
                    }
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
