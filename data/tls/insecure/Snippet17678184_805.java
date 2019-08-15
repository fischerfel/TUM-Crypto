 Allow unsafe renegotiation: false
    Allow legacy hello messages: true
    Is initial handshake: true
    Is secure renegotiation: false
    main, setSoTimeout(120) called
    %% No cached client session
    ClientHello, TLSv1 
    RandomCookie: GMT: 1357201614 bytes = { 70, 133, 164, 224, 89, 101, 204, 41, 107, 201, 176, 66, 93, 118, 139, 59, 50, 176, 84, 197, 238, 236, 187, 211, 158, 43, 159, 112 }
    Session ID: {}
    Cipher Suites: SSL_RSA_WITH_RC4_128_MD5, SSL_RSA_WITH_RC4_128_SHA, TLS_RSA_WITH_AES_128_CBC_SHA, TLS_DHE_RSA_WITH_AES_128_CBC_SHA, TLS_DHE_DSS_WITH_AES_128_CBC_SHA, SSL_RSA_WITH_3DES_EDE_CBC_SHA, SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA, SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA, SSL_RSA_WITH_DES_CBC_SHA, SSL_DHE_RSA_WITH_DES_CBC_SHA, SSL_DHE_DSS_WITH_DES_CBC_SHA, SSL_RSA_EXPORT_WITH_RC4_40_MD5, SSL_RSA_EXPORT_WITH_DES40_CBC_SHA, SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA, SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA, TLS_EMPTY_RENEGOTIATION_INFO_SCSV
    Compression Methods: { 0 }
    ***
    **main, WRITE: TLSv1 Handshake, length = 75
    main, READ: TLSv1 Alert, length = 2
    main, RECV TLSv1 ALERT: fatal, handshake_failure
    main, called closeSocket()**




**My source code:**





    public class **SumanLdapTest1**{
        public static void main(String args[]){
       try{
                //System.setProperty("ldaps.protocols", "TLSv1");
                 System.out.println("here");
                 DirContext ctx = null;
            String host="slc00ahj.us.oracle.com"; 
            String port="3131";
            String userName="cn=orcladmin";
            String password="welcome1";
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                            "com.sun.jndi.ldap.LdapCtxFactory");
            //env.put(Context.PROVIDER_URL, "ldap://" + host + ":"+ port+ "/");
            env.put(Context.SECURITY_PRINCIPAL, userName);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put("com.sun.jndi.ldap.connect.timeout", "120");
            env.put(Context.PROVIDER_URL, "ldaps://" + host
                            + ":" + port);
            env.put(Context.SECURITY_PROTOCOL, "ssl");
            env.put("java.naming.ldap.factory.socket","**SumanSSLFactory**");
            ctx = new InitialDirContext(env);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public class SumanSSLFactory extends SSLSocketFactory {
            private SSLSocketFactory factory = null;
        private Exception exception = null;

        public SumanSSLFactory() {
            System.out.println("LdapSSLFactory initialization started...");


            try {
                this.factory = **getSSLSocketFactory**();
            } catch (Exception ex) {
                            ex.printStackTrace();
                System.out.println("LDAPSSLFactory Initialization error");
                this.factory = null;
                this.exception = ex;
            }

            System.out.println("LdapSSLFactory Initialization completed.");
        }

        public SSLSocket createSocket() throws IOException {
            System.out.println("LdapSSLFactory.createSocket()");
            if (this.factory == null)
                throw new IOException();
            SSLSocket st=null;
            try{


                    (new Throwable()).printStackTrace();
                     st=(SSLSocket)this.factory.createSocket();
                    st.setEnabledProtocols( new String[] { "TLSv1", "SSLv3" } );  

                         }catch(Exception e){
                             e.printStackTrace();
                         }

            return st;
        }


        private SSLSocketFactory **getSSLSocketFactory**()
                        {
                SSLSocketFactory sslSocketFactory = null;
                System.out.println("Using Non Authenticated SSL Mechanism.");
                try {
                        TrustManager[] tmA = { new X509TrustManager() {
                                public X509Certificate[] getAcceptedIssuers() {
                                        X509Certificate[] issuers = new X509Certificate[0];
                                        return issuers;
                                    //return null;
                                }

                                public void checkClientTrusted(X509Certificate[] chain,
                                                String authType) throws CertificateException {
                                }

                                public void checkServerTrusted(X509Certificate[] chain,
                                                String authType) throws CertificateException {
                                }
                        } };

                        // get the SSLContext and factory
                        SSLContext ctx = SSLContext.getInstance("TLS");
                        ctx.init(null, tmA, null);
                        sslSocketFactory = ctx.getSocketFactory();
                   // System.setProperty("ldaps.protocols", "TLSv1");
                    System.out.println("SSOSocketUtil factory created sslSocketFactory"+sslSocketFactory);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("SSOSocketUtil factory exception");

                }

                return sslSocketFactory;
        }

    }

Any help on this will be appreciated    
