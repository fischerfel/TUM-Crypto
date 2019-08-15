byte[] localcert = Base64.decode(
            "MIID5TCCAc2gAwIBAgICEAEwDQYJKoZIhvcNAQELBQAwMzELMAkGA1UEBhMCc2cx" +
                    "CzAJBgNVBAgMAnNnMRcwFQYDVQQKDA5pbnRlcm1lZGlhdGVDQTAeFw0xNjExMjgw" +
                    "NDAzMjdaFw0xNzEyMDgwNDAzMjdaMDsxCzAJBgNVBAYTAnNnMQswCQYDVQQIEwJz" +
                    "ZzELMAkGA1UEBxMCc2cxEjAQBgNVBAoTCWxvY2FsaG9zdDBZMBMGByqGSM49AgEG" +
                    "CCqGSM49AwEHA0IABDuhAyMw6OilNmfWo1v6b8XwU8xbQm0Sy/I9qpdC4+qDToSl" +
                    "EOe+vw7GiVgONTJz2gwMW+VgoGp49aM5GTPo39ujgcUwgcIwCQYDVR0TBAIwADAR" +
                    "BglghkgBhvhCAQEEBAMCBaAwMwYJYIZIAYb4QgENBCYWJE9wZW5TU0wgR2VuZXJh" +
                    "dGVkIENsaWVudCBDZXJ0aWZpY2F0ZTAdBgNVHQ4EFgQUnbUGm/qaO1JbhY+qVlXw" +
                    "BewUI/swHwYDVR0jBBgwFoAUPSzKlcBTp0pCQ290SlDLmIQS+/0wDgYDVR0PAQH/" +
                    "BAQDAgXgMB0GA1UdJQQWMBQGCCsGAQUFBwMCBggrBgEFBQcDBDANBgkqhkiG9w0B" +
                    "AQsFAAOCAgEAJj0J2A3AAcrRw02ZzQsEC4nTyd05krF4oRFo0JODlzNiKaOhQt76" +
                    "Va427cpVUZwmjb/f1We+AjLJgQiEfnuD7JPSvXHLQTbXNDMgpZ9HXHZoXYfH+2h7" +
                    "MGvw6Qkj4lC10q9UC14rDSD/ZsR1J0mQCQuOIBRFNOkSPiSUu4zouCD3xv5uZVXR" +
                    "mimhJ1zgqSYF4LHegJAVwrowMsuaeQXybrIQ+/LJ8HXf8McvPZwtQTuoN/q5zHXz" +
                    "l+7q4nglyVY+TXPAdwyha0Yq2p0z0jdWm5UpEehmIpXtJghNtcCCRfb48flfZ/B7" +
                    "JW9VrlcjScOtQfSOrElYgwJ8MlUTzz7oWgbbVp9uNQZeAQQPeOQYLAvSNchPnLiP" +
                    "ftPuICW2siDeFC42lwYsDYR/9sYs7/gzL79i7bHrdMJ07brXw30hb1r6Vu9a+sHF" +
                    "D087NxHv33u22+W/2PMLDE89MynTC3H3gWvyzGIky0/kYSpZO/xZuFrg0jIJu0lH" +
                    "9b7jw1hQM1nDkTO5Gn2wJuaHaiZ22tMr47e4Xlkctal4hAA4Ya1uBXuMuwy0BC8q" +
                    "nLLxCLBcJJPAyIG2LvIT2vdWIP0Gz84mHKDbOPekHmXIF3bHE4pPeyDIJ+w00UoM" +
                    "xJdedT5BJarqEpiQtrGn4FBh3fsnHFXyNnNMCIylCvbg0Ij/AsQJCpg=");

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate x509Certificate= (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(localcert));
    PublicKey publicKey=x509Certificate.getPublicKey();

    byte[] pkey=Base64.decode("MHcCAQEEINmVG7z3YutAqRYZ5iAaJSXcP+GJWjtmSx3ba6RfKkJQoAoGCCqGSM49" +
            "AwEHoUQDQgAEO6EDIzDo6KU2Z9ajW/pvxfBTzFtCbRLL8j2ql0Lj6oNOhKUQ576/" +
            "DsaJWA41MnPaDAxb5WCganj1ozkZM+jf2w==");
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("prime256v1");
    KeyFactory kf = KeyFactory.getInstance("ECDSA", new BouncyCastleProvider());
    ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(new BigInteger(1, pkey), spec);
    ECPrivateKey privkey= (ECPrivateKey) kf.generatePrivate(ecPrivateKeySpec);

    String name = "prime256v1";

    //  generate derivation and encoding vectors
    byte[]  d = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    byte[]  e = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1 };
    IESParameterSpec param = new IESParameterSpec(d, e, 256);

    Cipher iesCipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
    //Encrypt
    iesCipher.init(Cipher.ENCRYPT_MODE, publicKey, param);
    byte[] enc= iesCipher.doFinal("TestECIES".getBytes());
    System.out.println(new String(enc));
    //Decrypt
    iesCipher.init(Cipher.DECRYPT_MODE, privkey, param);
    byte[] decry=iesCipher.doFinal(enc);
    System.out.println(new String(decry));
