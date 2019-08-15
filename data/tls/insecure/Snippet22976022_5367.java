public class ServerThread extends Thread
{
    //is the thread running
    private boolean running = true;

    //ports for the server sockets
    private final int dataPort;
    private final int filePort;

    private final String certificateDir;
    private final char[] password;

    private Vector<ClientHandlerThread> connectedClients = new Vector<ClientHandlerThread>(20, 5);
    private Properties userProperties = new Properties();

    public ServerThread(int dataPort,
                        int filePort,
                        String certificateDir,
                        char[] password,
                        Properties userProperties)
    {
        this.dataPort = dataPort;
        this.filePort = filePort;
        this.certificateDir = certificateDir;
        this.password = password;
        this.userProperties = userProperties;
    }

    public void run()
    {
        /*
         * We need a server socket that can accept traffic. I use one for file traffic and one
         * for data traffic although one socket could be used.
         */
        SSLServerSocket sslDataTraffic = null;
        SSLServerSocket sslFileTraffic = null;
        SSLServerSocketFactory sslFac = null;

        /*
         * Everything in the following block is related to creating a SSL security manager. 
         * If you don't need validated communications you don't have to use SSL. Just normal
         * sockets.
         */
        try
        {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(certificateDir), password);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance((KeyManagerFactory.getDefaultAlgorithm()));
            kmf.init(keyStore, password);

            System.setProperty("https.protocols", "SSL");
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            sslFac = ctx.getServerSocketFactory();
        }
        catch(Exception e)
        {
            System.out.println("FAILED.");
            e.printStackTrace();
            System.exit(-1);
        }

        try
        {
            //create data server socket 
            System.out.print("Creating data socket......... ");
            sslDataTraffic = (SSLServerSocket) sslFac.createServerSocket(dataPort);
            System.out.println("DONE. Est. on:" + dataPort);

            //create file server socket
            System.out.print("Creating file socket......... ");
            sslFileTraffic = (SSLServerSocket) sslFac.createServerSocket(filePort);
            System.out.println("DONE. Est. on:" + filePort);
        }
        catch (IOException e)
        {
            System.out.println("FAILED.");
            System.out.println(e.toString() + " ::: " + e.getCause());
            System.exit(-1);
        }

        /*
         * This block is used to print the ip the server is running on. Easy to incorporate this here
         * so the information doesn't have to be gathered form another source.
         */
        try
        {
            System.out.print("Finishing.................... ");
            Socket s = new Socket("google.com", 80);
            System.out.println("DONE.");
            System.out.println("Server online at: " + s.getLocalAddress().getHostAddress());
            System.out.println("====================*====================");
            s.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        /*
         * This is the block that accepts connections from clients.
         */
        try
        {
            while (running)
            {
                //wait here until a connection is bound to new sockets through the server sockets
                SSLSocket sslDataTrafficSocketInstance = (SSLSocket) sslDataTraffic.accept();
                SSLSocket sslFileTrafficSocketInstance = (SSLSocket) sslFileTraffic.accept();
                //sockets to communicate with the client are created. Lets put them in a thread so 
                //we can continue to accept new clients while we work with the newly and previously
                //connected clients

                //create a new thread
                ClientHandlerThread c = new ClientHandlerThread(
                        sslDataTrafficSocketInstance, 
                        sslFileTrafficSocketInstance, 
                        userProperties);
                //start thread
                c.start();
                //add newly connected client to the list of connected clients
                connectedClients.add(c);
            }
        }
        catch (IOException e)
        {
            System.out.println("Fatal server error, terminating server and client handler threads");

            stopServer();
        }
    }
}
