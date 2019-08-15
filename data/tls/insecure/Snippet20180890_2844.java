public class ServerThread extends Thread
{
    private Vector<ClientHandlerThread> connectedClients = new Vector<ClientHandlerThread>(20, 5);

   public void run()
   {
    SSLServerSocket sslDataTraffic = null;
    SSLServerSocket sslFileTraffic = null;
    SSLServerSocketFactory sslFac = null;

    try
    {
        System.out.print("Validating SSL certificate... ");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(certificateDir), password);
        System.out.println("DONE.");

        System.out.print("Creating trust engine........ ");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        System.out.println("DONE.");

        System.out.print("Creating key engine.......... ");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance((KeyManagerFactory.getDefaultAlgorithm()));
        kmf.init(keyStore, password);
        System.out.println("DONE.");

        System.out.print("Creating SSL context......... ");
        System.setProperty("https.protocols", "SSL");
        SSLContext  ctx = SSLContext.getInstance("SSL");
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        sslFac = ctx.getServerSocketFactory();
        System.out.println("DONE.");
    }
    catch (Exception e) {}

    try
    {
        System.out.print("Creating data socket......... ");
        sslDataTraffic = (SSLServerSocket) sslFac.createServerSocket(dataPort);
        System.out.println("DONE. Est. on:" + dataPort);
    }
    catch (IOException e)
    {
        System.out.println("FAILED.");
        System.out.println(e.toString() + " ::: " + e.getCause());
        System.exit(-1);
    }

    try
    {
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

    while (running)
        {
            SSLSocket sslDataTrafficSocketInstance = (SSLSocket) sslDataTraffic.accept();
            SSLSocket sslFileTrafficSocketInstance = (SSLSocket) sslFileTraffic.accept();
            ClientHandlerThread c = new ClientHandlerThread(sslDataTrafficSocketInstance, sslFileTrafficSocketInstance);
            c.start();
            connectedClients.add(c);
        }
}
