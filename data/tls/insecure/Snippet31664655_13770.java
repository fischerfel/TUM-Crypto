public final class SSL_Context {
    private static SSL_Context instance = new SSL_Context();

public static SSL_Context getInstance() {
    return instance;
}

private SSLContext sslContext = null;
private SSL_Context() {
    try {
        sslContext = generateSSLContext();
    }
    catch (Exception e)
    {
        ErrorLogger.logException(e);
    }
}

final private void dumpKeyStore(KeyStore keyStore)
{
    try {
        // List the aliases
        Enumeration aliases = keyStore.aliases();
        for (; aliases.hasMoreElements(); ) {
            String alias = (String) aliases.nextElement();

            // Does alias refer to a private key?
            boolean a = keyStore.isKeyEntry(alias);

            // Does alias refer to a trusted certificate?
            boolean b = keyStore.isCertificateEntry(alias);
            ErrorLogger.log(alias + " " + a + " " + b, 2);
        }
    } catch (Exception e) {
        ErrorLogger.logException(e);
    }
}


final private KeyStore convertPEMToPKCS12(final String keyAndPubFile, final String chainFile, final String password) {
    try {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        PrivateKey key;
        Certificate pubCert;

        try (FileReader reader = new FileReader(keyAndPubFile);
             PEMParser pem = new PEMParser(reader)) {
            PEMKeyPair pemKeyPair = ((PEMKeyPair) pem.readObject());
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
            key = keyPair.getPrivate();


            X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();
            pubCert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
        }

        // Get the certificates
        try (FileReader reader = new FileReader(chainFile);
             PEMParser pem = new PEMParser(reader)) {

            //load all certs
            LinkedList<Certificate> certsll = new LinkedList<>();
            X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();
            do {
                Certificate X509Certificate = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
                certsll.add(X509Certificate);
            }
            while ((certHolder = (X509CertificateHolder) pem.readObject()) != null);

            Certificate[] chain = new Certificate[certsll.size()+1];
            chain[0] = pubCert;

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(null);

            int i = 1;
            for (Certificate cert : certsll) {
                ks.setCertificateEntry("chain" + i, cert);
                chain[i] = ks.getCertificate("chain" + i);
                i++;
            }

            ks.setKeyEntry("cert", key, password.toCharArray(), chain);

            return ks;
        }
    }
    catch (Exception e)
    {
        ErrorLogger.logException(e);
    }
    return null;
}

final private SSLContext generateSSLContext()
{
    String keyStorePassword = "";
    try {
        KeyStore keyStore = convertPEMToPKCS12("ssl/keyandcert.pem", "ssl/ca_bundle.crt", keyStorePassword);
        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
        sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        return sslContext;

    } catch (Exception e) {
        ErrorLogger.logException(e);
    }
    return null;
}

final public SSLContext getContext() {
    return sslContext;
}

final public static void main(String args[])
{
        getInstance().getContext();
}

}
