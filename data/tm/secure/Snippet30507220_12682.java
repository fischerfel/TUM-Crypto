X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                    for (TrustManager tm : managers) {
                        if (tm instanceof X509TrustManager) {
                            ((X509TrustManager) tm).checkClientTrusted(
                                    chain, authType);
                        }
                    }
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                    for (X509Certificate cert : chain) {
                        // cert gives the server Certificate Information.
                        if (cert.getIssuerX500Principal().equals(
                                trustedRoot.getIssuerX500Principal())) {
                            return;
                        }
                    }
                    for (TrustManager tm : managers) {
                        if (tm instanceof X509TrustManager) {
                            ((X509TrustManager) tm).checkServerTrusted(
                                    chain, authType);
                        }
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    ArrayList<X509Certificate> issuers = new ArrayList<>();
                    for (TrustManager tm : managers) {
                        if (tm instanceof X509TrustManager) {
                            issuers.addAll(Arrays
                                    .asList(((X509TrustManager) tm)
                                            .getAcceptedIssuers()));
                        }
                    }
                    return issuers.toArray(new X509Certificate[issuers
                            .size()]);
                }

            };
