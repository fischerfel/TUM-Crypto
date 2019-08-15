SSLContext ctx = SSLContext.getInstance("SSL");
HTTPSProperties prop = new HTTPSProperties(
new HostnameVerifier () {
    public boolean verify(String hostname, SSLSession session) {
        System.out.println("\n\nFAKE_Verifier: " + hostname+"\n\n");
        return true;
    }
}, ctx);
config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, prop);
