Bus bus = CXFBusFactory.getThreadDefaultBus();
bus.setExtension(new HTTPConduitConfigurer() {

    @Override
    public void configure(String name, String address, HTTPConduit conduit) {
        //set conduit parameters ...

        // ex. disable host name verification
        TLSClientParameters clientParameters = new TLSClientParameters();
        clientParameters.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        conduit.setTlsClientParameters(clientParameters);
    }
}, HTTPConduitConfigurer.class);

JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance(bus);
Client client = dcf.createClient(wsdlUri);
