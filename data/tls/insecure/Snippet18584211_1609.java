TrustStrategy easyStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    // eh, why not?
                    return true;
                }
            };

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            SSLSocketFactory ssf = new SSLSocketFactory(easyStrategy);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
