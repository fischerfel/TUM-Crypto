private Session connect(SMTPTask task) {

    SSLSocketFactory factory = null;
    try {
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] passphrase = "changeit".toCharArray();

        ctx = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("truststore.jks"), passphrase);

        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);

        factory = ctx.getSocketFactory();
    } catch (Exception e) {
        log.error("Error with SSLFactory",e);
    }

    String host = getHost();
    String port = String.valueOf(getPort());

    Authenticator authenticator = new Authenticator(task);


    Properties properties = new Properties();
    properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
    properties.setProperty("mail.smtp.auth", "true");
    //properties.setProperty("mail.imap.auth.login.disable", "true");
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.port", port);
    properties.put("mail.imaps.ssl.socketFactory", factory);
    properties.setProperty("mail.imap.port", "993");

    Properties systemProps = System.getProperties();
    systemProps.put( "javax.net.ssl.trustStore", "truststore.jks");
    systemProps.put( "javax.net.ssl.trustStorePassword", "changeit");
    System.setProperties(systemProps);

    return Session.getInstance(properties, authenticator);
