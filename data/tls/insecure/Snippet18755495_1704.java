private static final byte[] CACERTROOTDER = new byte[]{
        48, -126, 7, 61, 48, -126, 5, 37, -96, 3, 2, 1, 2, 2, 1, 0,
        // ...
        };

/**
 * Read x509 certificated file from byte[].
 *
 * @param bytes certificate in der format
 * @return certificate
 */
private static X509Certificate getCertificate(final byte[] bytes)
        throws IOException, CertificateException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate ca;
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    try {
        ca = (X509Certificate) cf.generateCertificate(is);
        Log.d(TAG, "ca=", ca.getSubjectDN());
    } finally {
        is.close();
    }
    return ca;
}

/**
 * Trust only CAcert's CA. CA cert is injected as byte[]. Following best practices from
 * https://developer.android.com/training/articles/security-ssl.html#UnknownCa
 */
private static void trustCAcert()
        throws KeyStoreException, IOException,
        CertificateException, NoSuchAlgorithmException,
        KeyManagementException {
    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    final KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("CAcert-root", getCertificate(CACERTROOTDER));
    // if your HTTPd is not sending the full chain, add class3 cert to the key store
    // keyStore.setCertificateEntry("CAcert-class3", getCertificate(CACERTCLASS3DER));

    // Create a TrustManager that trusts the CAs in our KeyStore
    final TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    SSLContext sslContext = SSLContext.getInstance("TLS");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        // may work on HC+, but there is no AVD or device to test it
        sslContext.init(null, tmf.getTrustManagers(), null);
    } else {
        // looks like CLR is broken in lower APIs. implement out own checks here :x
        // see https://stackoverflow.com/q/18713966/2331953
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(final String hostname, final SSLSession session) {
                try {
                    // check if hostname matches DN
                    String dn = session.getPeerCertificateChain()[0].getSubjectDN().toString();

                    Log.d(TAG, "DN=", dn);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        return dn.equals("CN=" + hostname);
                    } else {
                        // no SNI on API<9, but I know the first vhost's hostname
                        return dn.equals("CN=" + hostname)
                                || dn.equals("CN=" + hostname.replace("jsonrpc", "rest"));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "unexpected exception", e);
                    return false;
                }
            }
        });

        // build our own trust manager
        X509TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                // nothing to do
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                    final String authType)
                    throws CertificateException {
                // nothing to do
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                    final String authType) throws CertificateException {
                // nothing to do
                Log.d(TAG, "checkServerTrusted(", chain, ")");
                X509Certificate cert = chain[0];

                cert.checkValidity();

                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
                list.add(cert);
                CertPath cp = cf.generateCertPath(list);
                try {
                    PKIXParameters params = new PKIXParameters(keyStore);
                    params.setRevocationEnabled(false); // CLR is broken, remember?
                    CertPathValidator cpv = CertPathValidator
                            .getInstance(CertPathValidator.getDefaultType());
                    cpv.validate(cp, params);
                } catch (KeyStoreException e) {
                    Log.d(TAG, "invalid key store", e);
                    throw new CertificateException(e);
                } catch (InvalidAlgorithmParameterException e) {
                    Log.d(TAG, "invalid algorithm", e);
                    throw new CertificateException(e);
                } catch (NoSuchAlgorithmException e) {
                    Log.d(TAG, "no such algorithm", e);
                    throw new CertificateException(e);
                } catch (CertPathValidatorException e) {
                    Log.d(TAG, "verification failed");
                    throw new CertificateException(e);
                }
                Log.d(TAG, "verification successful");
            }
        };
        sslContext.init(null, new X509TrustManager[]{tm}, null);
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
}
