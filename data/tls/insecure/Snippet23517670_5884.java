public static void main22(String args[]) {

try {

    System.setProperty("javax.net.ssl.keyStore",
            "D://certificate//latest//client-pkcs-12-cert");
    System.setProperty("javax.net.ssl.keyStorePassword", "keypass");

    HashMap<String, Object> env = new HashMap<String, Object>();
    String truststore = "D://certificate//latest//client-pkcs-12-cert";
    char truststorepass[] = "keypass".toCharArray();
    KeyStore ks = KeyStore.getInstance("pkcs12");
    ks.load(new FileInputStream(truststore), truststorepass);

    TrustManagerFactory tmf = TrustManagerFactory
            .getInstance("SunX509");

    tmf.init(ks);
    SSLContext ctx = SSLContext.getInstance("TLSv1");
    ctx.init(null, tmf.getTrustManagers(), null);
    SSLSocketFactory ssf = ctx.getSocketFactory();

    env.put("jmx.remote.tls.socket.factory", ssf);

    JMXServiceURL address = new JMXServiceURL("rmi", "", 0,
            "/jndi/rmi://localhost:9191/jmxrmi");

    JMXConnector jmxc = JMXConnectorFactory.connect(address, env);
    MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
    Set<ObjectInstance> beans = mbsc.queryMBeans(null, null);

    for (ObjectInstance instance : beans) {
        MBeanInfo info = mbsc.getMBeanInfo(instance.getObjectName());
        System.out.println(info);
    }

    jmxc.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("error :" + e.getMessage());
    }
}
