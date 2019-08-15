    private Session connect(SMTPTask task) {

    log.debug("Connecting to SMTP");
    Properties props = new Properties();
    Properties systemProps = System.getProperties();

    if (task.getKeyStorePath().contains("None")) {
        systemProps.remove("javax.net.ssl.trustStore");
        systemProps.remove("javax.net.ssl.trustStorePassword");
        System.setProperties(systemProps);
        props.remove("mail.smtp.ssl.socketFactory");
    } else {

        systemProps.put("javax.net.ssl.trustStore", "src/main/resources/stores/" + task.getKeyStorePath());
        systemProps.put("javax.net.ssl.trustStorePassword", "changeit");
        System.setProperties(systemProps);

        SSLSocketFactory factory = null;
        try {
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "changeit".toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");

            URL resource = this.getClass().getResource("/stores/" + task.getKeyStorePath());
            File file = new File(resource.toURI());

            FileInputStream fis = new FileInputStream(file);
            ks.load(fis, passphrase);
            fis.close();
            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            factory = ctx.getSocketFactory();

            props.put("mail.smtp.ssl.socketFactory", factory);
        } catch (Exception e) {
            log.error("Error with SSLFactory", e);
        }
    }

    String host = task.getHost();
    String port = String.valueOf(task.getPort());

    props.put("mail.smtps.host", host);
    props.put("mail.smtps.port", port);
    props.put("mail.smtps.auth", "true");
    props.put("mail.imaps.fetchsize", "22020096");

    return Session.geInstance(props);
    }
