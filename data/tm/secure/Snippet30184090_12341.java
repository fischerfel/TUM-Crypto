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
                        String authType) {

                    for (X509Certificate cert : chain) {

                        final String mCertificatinoType = cert.getType();
                        Date afterDate = cert.getNotAfter();
                        Date beforeDate = cert.getNotBefore();
                        Date currentDate = new Date();

                        try {
                            cert.checkValidity(new Date());
                        } catch (CertificateExpiredException e) {
                            LoginActivity.isExpired = true;
                            e.printStackTrace();
                        } catch (CertificateNotYetValidException e) {
                            LoginActivity.isInValid = true;
                            e.printStackTrace();
                        }

                        try {
                            cert.verify(trustedRoot.getPublicKey());
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchProviderException e) {
                            e.printStackTrace();
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (cert.getIssuerX500Principal().equals(
                                    trustedRoot.getIssuerX500Principal())) {

                            }
                            cert.verify(trustedHost.getPublicKey());
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchProviderException e) {
                            e.printStackTrace();
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        }

                        if (afterDate.compareTo(currentDate)
                                * currentDate.compareTo(beforeDate) > 0) {
                        } else {

                        }

                        if (cert.getIssuerX500Principal().equals(
                                trustedRoot.getIssuerX500Principal())) {
                            return;
                        }
                    }

                    // for (X509Certificate cert : chain) {
                    // URL url;
                    // String host = "";
                    // if (baseHostString.equalsIgnoreCase("")) {
                    // final Settings settings = mApplication
                    // .getSettings();
                    // try {
                    // url = new URL(
                    // settings.serverAddress.toString());
                    // host = url.getAuthority();
                    // } catch (MalformedURLException e) {
                    // e.printStackTrace();
                    // }
                    // } else {
                    //
                    // }
                    //
                    // String dn = cert.getSubjectDN().getName();
                    // String CN = getValByAttributeTypeFromIssuerDN(dn,
                    // "CN=");
                    // if (CN.equalsIgnoreCase(host)) {
                    // if (cert.getIssuerX500Principal().equals(
                    // trustedRoot.getIssuerX500Principal())) {
                    // return;
                    // } else {
                    // }
                    // } else {
                    // }
                    // }
                    for (TrustManager tm : managers) {
                        if (tm instanceof X509TrustManager) {
                            try {
                                ((X509TrustManager) tm).checkServerTrusted(
                                        chain, authType);
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            }
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
