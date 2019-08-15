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
                public void checkServerTrusted(
                        final X509Certificate[] chain, String authType) {

                    for (X509Certificate cert : chain) {

                        final String mCertificatinoType = cert.getType();
                        Date afterDate = cert.getNotAfter();
                        Date beforeDate = cert.getNotBefore();
                        Date currentDate = new Date();

                        try {
                            cert.checkValidity(new Date());
                        } catch (CertificateExpiredException e) {
                            isExpired = true;
                            e.printStackTrace();
                        } catch (CertificateNotYetValidException e) {
                            isInValid = true;
                            e.printStackTrace();
                        }

                        if (afterDate.compareTo(currentDate)
                                * currentDate.compareTo(beforeDate) > 0) {
                            isExpired = false;
                        } else {
                            isExpired = true;
                        }

                        String dn = cert.getSubjectDN().getName();
                        String CN = getValByAttributeTypeFromIssuerDN(dn,
                                "CN=");

                        String host = "";
                        if (TextUtils.isEmpty(query)) {
                            if (baseHostString.equalsIgnoreCase("")) {
                                final Settings settings = mApplication
                                        .getSettings();
                                try {
                                    URL url = new URL(
                                            settings.serverAddress
                                                    .toString());
                                    host = url.getAuthority();
                                    if (host.contains(String.valueOf(url
                                            .getPort()))) {
                                        String toBeReplaced = ":"
                                                + url.getPort();
                                        host = host.replace(toBeReplaced,
                                                "");
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    URL url = new URL(baseHostString);
                                    host = url.getAuthority();
                                    if (host.contains(String.valueOf(url
                                            .getPort()))) {
                                        String toBeReplaced = ":"
                                                + url.getPort();
                                        host = host.replace(toBeReplaced,
                                                "");
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                URL url = new URL(query);
                                host = url.getAuthority();
                                if (host.contains(String.valueOf(url
                                        .getPort()))) {
                                    String toBeReplaced = ":"
                                            + url.getPort();
                                    host = host.replace(toBeReplaced, "");
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }

                        if (CN.equalsIgnoreCase(host)) {
                            isHostMisMatch = false;
                        } else {
                            isHostMisMatch = true;
                        }

                        for (TrustManager tm : managers) {
                            if (tm instanceof X509TrustManager) {
                                try {
                                    ((X509TrustManager) tm)
                                            .checkServerTrusted(chain,
                                                    authType);
                                } catch (CertificateException e) {
                                    if (e.getMessage() != null
                                            && e.getMessage()
                                                    .contains(
                                                            "Trust anchor for certification path not found.")) {
                                        isNotTrusted = true;
                                        mApplication
                                                .setCurrentCertificate(chain);
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (cert.getIssuerX500Principal().equals(
                                trustedRoot.getIssuerX500Principal())) {
                            return;
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
