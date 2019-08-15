    URL server = new URL("http://[my_openscep_server]/cgi-bin/pkiclient.exe");

    KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    char[] keyPw = "somePW".toCharArray();
    SecretKeyFactory factory = SecretKeyFactory.getInstance(FACTORY_ALGORITHM, PROVIDER);
    KeySpec spec = new PBEKeySpec(keyPw, SecureRandom.getSeed(50), 65536, 128);
    SecretKey tmp = factory.generateSecret(spec);

    KeyStore ks = KeyStore.getInstance("JCEKS", "SunJCE");
    ks.load(null, keyPw);
    ks.setEntry("someAlias", new SecretKeyEntry(tmp), new KeyStore.PasswordProtection(keyPw));

    X500Name entityName = new X500Name("DN=someDN");

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, -1);
    Date notBefore = calendar.getTime();

    calendar.add(Calendar.HOUR, 4);
    Date notAfter = calendar.getTime();

    X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(entityName, BigInteger.valueOf(
            new Date().getTime()).abs(), notBefore, notAfter, entityName, SubjectPublicKeyInfo.getInstance(keyPair
            .getPublic().getEncoded()));

    ContentSigner cs = new JCESigner(keyPair.getPrivate(), "SHA256withRSA");
    byte[] certBytes = certBuilder.build(cs).getEncoded();
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    X509Certificate certificate = (X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));

    PKCS10CertificationRequestBuilder crb = new JcaPKCS10CertificationRequestBuilder(
    certificate.getSubjectX500Principal(), keyPair.getPublic());
    PKCS10CertificationRequest csr = crb.build(cs);

    // Send the enrolment request
    CallbackHandler handler = new DefaultCallbackHandler(new ConsoleCertificateVerifier());

    Client client = new Client(server, handler);
    client.enrol(certificate, keyPair.getPrivate(), csr);
