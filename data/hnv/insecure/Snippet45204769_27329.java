public class SampleClient {


    public static final Locale ENGLISH = Locale.ENGLISH;
    public static final Locale ENGLISH_FINLAND = new Locale("en", "FI");
    public static final Locale ENGLISH_US = new Locale("en", "US");

    public static final Locale FINNISH = new Locale("fi");
    public static final Locale FINNISH_FINLAND = new Locale("fi", "FI");

    public static final Locale GERMAN = Locale.GERMAN;
    public static final Locale GERMAN_GERMANY = new Locale("de", "DE");

    public static void main(String[] args) 
    throws Exception {
//      if (args.length==0) {
//          System.out.println("Usage: SampleClient [server uri]");
//          return;
//      }
        //String url = /*args[0]*/"opc.tcp://uademo.prosysopc.com:53530/OPCUA/SimulationServer";
        //String url = /*args[0]*/"opc.tcp://uademo.prosysopc.com:53530";
        //String url = /*args[0]*/"opc.tcp://opcua.demo-this.com:51210/UA/SampleServer";
        String url="opc.tcp://mfactorengineering.com:4840";
        //String url="opc.tcp://commsvr.com:51234/UA/CAS_UA_Server";
        //String url="opc.tcp://uademo.prosysopc.com:53530/OPCUA/SimulationServer";
        //String url="opc.tcp://opcua.demo-this.com:51210/UA/SampleServer";
        //String url="opc.tcp://opcua.demo-this.com:51211/UA/SampleServer";
        //String url="opc.tcp://opcua.demo-this.com:51212/UA/SampleServer";
        //String url="opc.tcp://demo.ascolab.com:4841";
        //String url="opc.tcp://alamscada.dynu.com:4096";

        System.out.print("SampleClient: Connecting to "+url+" .. ");

        //////////////  CLIENT  //////////////
        // Create Client
        Application myApplication = new Application();
        Client myClient = new Client(myApplication);
        myApplication.addLocale( ENGLISH );
        myApplication.setApplicationName( new LocalizedText("Java Sample Client", Locale.ENGLISH) );
        myApplication.setProductUri( "urn:JavaSampleClient" );

        CertificateUtils.setKeySize(1024); // default = 1024
        KeyPair pair = ExampleKeys.getCert("SampleClient");
        myApplication.addApplicationInstanceCertificate( pair );        

        // The HTTPS SecurityPolicies are defined separate from the endpoint securities
        myApplication.getHttpsSettings().setHttpsSecurityPolicies(HttpsSecurityPolicy.ALL);

        // Peer verifier
        myApplication.getHttpsSettings().setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
        myApplication.getHttpsSettings().setCertificateValidator( CertificateValidator.ALLOW_ALL );

        // The certificate to use for HTTPS
        KeyPair myHttpsCertificate = ExampleKeys.getHttpsCert("SampleClient"); 
        myApplication.getHttpsSettings().setKeyPair( myHttpsCertificate );

        // Connect to the given uri
        SessionChannel mySession = myClient.createSessionChannel(url);
//      mySession.activate("username", "123");
        mySession.activate();
        //////////////////////////////////////      

        /////////////  EXECUTE  //////////////      
        // Browse Root
        BrowseDescription browse = new BrowseDescription();
        browse.setNodeId( Identifiers.RootFolder );
        browse.setBrowseDirection( BrowseDirection.Forward );
        browse.setIncludeSubtypes( true );
        browse.setNodeClassMask( NodeClass.Object, NodeClass.Variable );
        browse.setResultMask( BrowseResultMask.All );
        BrowseResponse res3 = mySession.Browse( null, null, null, browse );             
        System.out.println(res3);

        // Read Namespace Array
        ReadResponse res5 = mySession.Read(
            null, 
            null, 
            TimestampsToReturn.Neither,                 
            new ReadValueId(Identifiers.Server_NamespaceArray, Attributes.Value, null, null ) 
        );
        String[] namespaceArray = (String[]) res5.getResults()[0].getValue().getValue();
        System.out.println(Arrays.toString(namespaceArray));

        // Read a variable
        ReadResponse res4 = mySession.Read(
            null, 
            500.0, 
            TimestampsToReturn.Source, 
            new ReadValueId(new NodeId(6, 1710), Attributes.Value, null, null ) 
        );      
        System.out.println(res4);

        res4 = mySession.Read(
            null, 
            500.0, 
            TimestampsToReturn.Source, 
            new ReadValueId(new NodeId(6, 1710), Attributes.DataType, null, null ) 
        );      
        System.out.println(res4);


        /////////////  SHUTDOWN  /////////////
        mySession.close();
        mySession.closeAsync();
        //////////////////////////////////////  

    }

}
