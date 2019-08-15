public class DefaultsTest {

    static LocalTestServer server;

    // This sets up the server for use when testing the revocation code.
    @BeforeClass
    public static void setupServer() throws NoSuchAlgorithmException {

        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            server = new LocalTestServer(sslcontext);
            server.registerDefaultHandlers();
            server.start();
        }
        catch(Exception e)
        {
            // TODO
        }

        server.register("/sockettimeout", new HttpRequestHandler(){

            @Override 
            public void handle(HttpRequest request, HttpResponse response, HttpContext context)
throws HttpException, IOException {
                try {
                    Thread.sleep(Integer.MAX_VALUE); //sleep forever
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @AfterClass
    public static void teardown(){

        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to stop the server");
        }
    }

    public boolean testCertificateRevocation() throws CertificateEncodingException {

        // Details of running server need to be set.
        InetSocketAddress addr = server.getServiceAddress();
        String address = String.format("http://%s:%s/sockettimeout", addr.getHostName(), addr.getPort());



        try{

            // IN HERE - I NEED TO BE ABLE TO LOAD A CERTIFICATE
            // AND ALSO ADD A CERT TO THE REVOCATION LIST

            // AT THIS POINT I CAN USE THEM BOTH TO TEST MY CODE TO MAKE SURE IT WORKS! :)

        }
        finally
        {
            teardown();
        }

        return true;


    }
}
