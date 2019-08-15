try
{
    int ServerPort = 21;
    ServerSocket FtpExServer = new ServerSocket(ServerPort);
    while(true)
    {
        Socket S = FtpExServer.accept();
        InputStreamReader ISR = new InputStreamReader(S.getInputStream());
        OutputStreamWriter OSW = new OutputStreamWriter(S.getOutputStream());
        BufferedReader ClientSocketReader = new BufferedReader(ISR);
        PrintWriter ClientSocketWriter = new PrintWriter(OSW, true);

        ClientSocketWriter.println("220 Welcome to FTP server.");
        print(ClientSocketReader.readLine());
        ClientSocketWriter.println("234 AUTH TLS successful");

        char[] passphrase = "pass".toCharArray();
        char[] cpassphrase = "cpass".toCharArray();
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream("keystore.jks"), passphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keystore, cpassphrase);
        SSLContext context = SSLContext.getInstance("TLS");
        KeyManager[] keyManagers = kmf.getKeyManagers();
        context.init(keyManagers, null, null);
        SSLServerSocketFactory ssf = context.getServerSocketFactory();

        SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(990);
                ss.setSoTimeout(2000);          
                SSLSocket s = (SSLSocket)ss.accept();

        ISR = new InputStreamReader(s.getInputStream());
        OSW = new OutputStreamWriter(s.getOutputStream());
        ClientSocketReader = new BufferedReader(ISR);
        ClientSocketWriter = new PrintWriter(OSW, true);

        ClientSocketWriter.println("234 AUTH TLS successful");
        print(ClientSocketReader.readLine());
        ClientSocketWriter.println("331 Password required for smie");
        print(ClientSocketReader.readLine());
        ClientSocketWriter.println("230 User smie logged in");
        print(ClientSocketReader.readLine());
        ClientSocketWriter.println("215 UNIX Type: L8");
        print(ClientSocketReader.readLine());
        ClientSocketWriter.println("550 Command not suported.");
    }
}
catch(Exception e)
{
    print(e);
}
