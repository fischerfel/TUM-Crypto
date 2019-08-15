    String serverSpec = null;
    boolean enableAnonSuites = false;
    boolean isTracing = false;

    // Try and parse command line arguments.
    try {

        serverSpec = "ldap://10.47.16.60:389";
    }

    catch (Exception e) {
        trace(true,e.toString());
        usage();
        return;
    }

    try {

        // Create a SocketFactory that will be given to LDAP for 
        // building SSL sockets
        MySocketFactory msf = new MySocketFactory(isTracing,
                enableAnonSuites);

        // Set up environment for creating initial context
        Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
                "com.sun.jndi.ldap.LdapCtxFactory");


        // Must use the name of the server that is found in its certificate
        env.put(Context.PROVIDER_URL, 
                serverSpec
                );

        // Create initial context
        trace(isTracing,"Creating new Ldapcontext");
        LdapContext ctx = new InitialLdapContext(env, null);

        // Start 
        trace(isTracing,"Performing StartTlsRequest");
        StartTlsResponse tls = null;

        try {
            tls = (StartTlsResponse)ctx.extendedOperation(new StartTlsRequest());
        }
        catch (NamingException e) {
            trace(true,"Unable to establish SSL connection:\n"
                    +e);
            return;
        }


        // The default JSSE implementation will compare the hostname of
        // the server with the hostname in the server's certificate, and
        // will not proceed unless they match.  To override this behaviour,
        // you have to provide your own HostNameVerifier object.  The 
        // example below simply bypasses the check

        tls.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) 
            {
                return true;
            }
        });
        // Negotiate SSL on the connection using our own SocketFactory
        trace(isTracing,"Negotiating SSL");
        SSLSession sess = null;
        sess = tls.negotiate(msf);

        X509Certificate[] cert = sess.getPeerCertificateChain();
