public class LdapConnection
{

    private String host = "1.2.3.4"; //the correct ip...
    private String baseDn = "dc=x,dc=y,dc=com"; //the correct base DN

    private String username = "myUsername";
    private String password = "myPassword";

    private String connectionUrl = null;

    public void connectLdaps() throws Exception
    {
        connectionUrl = "ldaps://" + host + "/" + baseDn;

        System.out.println("Trying to connect to " + connectionUrl + " using LDAPS protocol");

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.ldap.derefAliases", "finding");
        env.put(Context.PROVIDER_URL, connectionUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put("java.naming.ldap.factory.socket", MySocketFactory.class.getName());

        new InitialLdapContext(env, null);

        System.out.println("Connected successfully!");
    }

    public static void main(String[] args) throws Exception
    {
        LdapConnection ldapConnection = new LdapConnection();
        ldapConnection.connectLdaps();
    }

}

public class MySocketFactory extends SocketFactory
{
    private static MySocketFactory instance = null;
    private SSLContext sslContext = null;

    private static String certFileName = "C:\\certs\\cert_by_hostname.cer";

    public static SocketFactory getDefault()
    {
        if (instance == null)
        {
            try
            {
                instance = new MySocketFactory();
                instance.initFactory();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Returning null socket factory");
            }
        }

        return instance;
    }

    private void initFactory() throws Exception
    {
        System.out.println("Initializing socket factory...");

        InputStream certStream = new FileInputStream(certFileName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate certificate = certificateFactory.generateCertificate(certStream);
        System.out.println("The certificate was generated. It is issued to " + ((X509Certificate)certificate).getSubjectDN());

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, "myPassword".toCharArray());
        new KeyStore.TrustedCertificateEntry(certificate);
        keyStore.setCertificateEntry("myCert", certificate);
        System.out.println("The Keystore was initialized.");

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
        trustManagerFactory.init(keyStore);
        System.out.println("The TrustManagerFactory was initialized.");

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        System.out.println("The SSLContext was initialized.");

    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException
    {
        System.out.println("In create socket, host is " + host + " and port is " + port);
        return sslContext.getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException
    {
        return sslContext.getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException
    {
        return sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
    {
        return sslContext.getSocketFactory().createSocket(address, port, localAddress, localPort);
    }

}
