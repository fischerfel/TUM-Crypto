KeyStore ts = KeyStore.getInstance("JKS");
FileInputStream tfis = new FileInputStream(trustStorePath);
ts.load(tfis, trustStorePass.toCharArray());

KeyManagerFactory kmf =  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

// initialize certification path checking for the offered certificates and revocation checks against CLRs
CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX");
PKIXRevocationChecker rc = (PKIXRevocationChecker)cpb.getRevocationChecker();
rc.setOptions(EnumSet.of(
    PKIXRevocationChecker.Option.PREFER_CRLS, // prefer CLR over OCSP
    PKIXRevocationChecker.Option.ONLY_END_ENTITY, 
    PKIXRevocationChecker.Option.SOFT_FAIL, // handshake should not fail when CRL is not available
PKIXRevocationChecker.Option.NO_FALLBACK)); // don't fall back to OCSP checking

PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(ts, new X509CertSelector());
pkixParams.addCertPathChecker(rc);

tmf.init( new CertPathTrustManagerParameters(pkixParams) );
// init KeyManagerFactory
kmf.init(...)

SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers), tmf.getTrustManagers(), null);
