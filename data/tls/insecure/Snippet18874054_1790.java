private SSLSocketFactory getSocketFactoryFromPEM(InputStream pemStream) throws Exception
{
    byte[] certAndKey = streamToBytes(pemStream);
    byte[] certBytes = parseDERFromPEM(certAndKey, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
    X509Certificate cert = generateCertificateFromDER(certBytes);

    KeyStore keystore = KeyStore.getInstance("JKS");

    String userHome = System.getProperty("user.home");
    String certPath = userHome + File.separator;
    int os = getOperatingSystem();
    switch (os)
    {
        case WINDOWS:
            //  <User Application Data Folder>\LocalLow\Sun\Java\Deployment\security\trusted.jssecacerts
            certPath += "AppData" + File.separator + "LocalLow" + File.separator + "Sun" + File.separator + "Java" + File.separator + "Deployment";
            break;
        case MAC:
            // ~/Library/Application Support/Oracle/Java/Deployment/security/trusted.jssecacerts
            certPath += "Library" + File.separator + "Application Support" + File.separator + "Oracle" + File.separator + "Java" + File.separator + "Deployment";
            break;
        case LINUX:
            // ${user.home}/.java/deployment/security/trusted.jssecacerts
            certPath += ".java" + File.separator + "deployment";
            break;
        default:
            break;
    }
    certPath += File.separator + "security" + File.separator + "trusted.jssecacerts";

    File certInputFile = new File(certPath);
    FileInputStream certInputStream = null;
    if (certInputFile.canRead())
    {
        certInputStream = new FileInputStream(certInputFile);
        keystore.load(certInputStream, null);
    }
    else
    {
        keystore.load(null);
    }

    keystore.setCertificateEntry("cert-alias", cert);

    FileOutputStream certOutputFile = new FileOutputStream(certInputFile);
    keystore.store(certOutputFile, "".toCharArray());
    certOutputFile.close();

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keystore);
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);
    return context.getSocketFactory();
}
