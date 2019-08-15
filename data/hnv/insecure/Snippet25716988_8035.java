InputStream rootCa = getResources().openRawResource(R.raw.root_ca);
CertificateFactory cf = CertificateFactory.getInstance("X.509");
trustStore.load(null,null);
trustStore.setCertificateEntry("serverRoot", cf.generateCertificate(rootCa));

SSLSocketFactory sf = new SSLSocketFactory(trustStore);
sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
