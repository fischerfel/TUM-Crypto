TrustManager trustManager = new X509TrustManager() {

                            @Override public void checkClientTrusted( X509Certificate[] x509Certificates, String s ) throws CertificateException {
                                    System.out.println( "=== interception point at checkClientTrusted ===" );
                                    System.out.println( x509Certificates[0].getSubjectDN().getName() );
                                    System.out.println( "================================================" );
                                    throw new CertificateException( "interception point at checkClientTrusted" );
                            }

                            @Override public void checkServerTrusted( X509Certificate[] x509Certificates, String s ) throws CertificateException {
                                    System.out.println( "checkServerTrusted" );
                            }

                            @Override public X509Certificate[] getAcceptedIssuers() {
                                    return new X509Certificate[0];
                            }
                    };
