public class ServerSide
{
ServerSocket serverSocket;
Socket regSocket;
Hashtable<String,User> userList;
CTC tempCTC;
File f;
DataInputStream in;
BufferedReader br;

SSLContext sslContext;
KeyManagerFactory keyManagerFactory;
KeyStore keyStore;
char[] keyStorePassphrase;

SSLServerSocketFactory sslServerSocketFactory;
SSLServerSocket sslServerSocket;
SSLSocket sslNormalSocket;

ServerSide()
{
    f = new File("userlist.txt");
    userList = new Hashtable<String, User>();
    loadUsers(f);

    try
    {
        sslContext = SSLContext.getInstance("SSL");
        keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyStore = KeyStore.getInstance("JKS");
        keyStorePassphrase = "passphrase".toCharArray();
        keyStore.load(new FileInputStream("testkeys"), keyStorePassphrase);
        keyManagerFactory.init(keyStore, keyStorePassphrase);
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        sslServerSocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

        sslServerSocket = (SSLServerSocket)sslServerSocketFactory.createServerSocket(12345);

        try{Thread.sleep(1000);}catch(Exception e){}

        while(true)
        {
            sslNormalSocket = (SSLSocket)sslServerSocket.accept();
            tempCTC = new CTC(sslNormalSocket, userList, f);
        }
    }
    catch(IOException ioe)
    {
        ioe.printStackTrace();
    }
    catch(NoSuchAlgorithmException nsae)
    {
        nsae.printStackTrace();
    }
    catch(KeyStoreException kse)
    {
        kse.printStackTrace();
    }
    catch(CertificateException ce)
    {
        ce.printStackTrace();
    }
    catch(UnrecoverableKeyException uke)
    {
        uke.printStackTrace();
    }
    catch(KeyManagementException kme)
    {
        kme.printStackTrace();
    }
}
