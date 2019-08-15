@Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) throws  CertificateException{
                 InputStream inStream;
                try {
                        inStream = new FileInputStream("E:\\Desktop\\cert\\domain.crt");
                        CertificateFactory cf = CertificateFactory.getInstance("X.509");
                        X509Certificate Mycert = (X509Certificate)cf.generateCertificate(inStream);
                        inStream.close();      

                        if (certs == null || certs.length == 0 || authType == null
                                || authType.length() == 0) {
                            throw new IllegalArgumentException("null or zero-length parameter");
                        }    

                             certs[0].verify(Mycert.getPublicKey());    

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    throw new CertificateException("error in validating certificate" , e);
                }

            }
