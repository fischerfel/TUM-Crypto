Hashtable<String,String> env = new Hashtable<String,String>();
env.put( Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory" );
env.put( Context.PROVIDER_URL, ( encryption == SSL ? "ldaps://" : "ldap://" ) + host + ":" + port );
if ( encryption == SSL ) {
    // env.put( "java.naming.ldap.factory.socket", "CustomSocketFactory" );
}
ctx = new InitialLdapContext( env, null );
if ( encryption != START_TLS )
    tls = null;
else {
    tls = (StartTlsResponse) ctx.extendedOperation( new StartTlsRequest() );
    tls.setHostnameVerifier( hostnameVerifier );
    tls.negotiate( sslContext.getSocketFactory() );
}
