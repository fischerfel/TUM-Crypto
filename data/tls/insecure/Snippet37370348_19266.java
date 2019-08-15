public class s implements Runnable {


List<SSLSocket> socketList= new ArrayList<SSLSocket>();
List<File> FileList= new ArrayList<File>();
List<Certificate> CertificateList = new ArrayList<Certificate>();

public static void main(String[] args) {
    s manager = new s();
    new Thread(manager).start();
    Scanner scanner = new Scanner(System.in);

    while(true){
        System.out.printf("Send> ");
        String message = scanner.nextLine();
        if(message.equals("") || message.equals("/n")){
            continue;
        }else{
            manager.send(message);
        }
    }
}

private static SSLServerSocket getServerSocket(int thePort)
    {
        SSLServerSocket s=null;
        try
        {
            String key="G:\\mySrvKeystore";
        char keyStorePass[]="123456".toCharArray();

        char keyPassword[]="123456".toCharArray();
        KeyStore ks= KeyStore.getInstance("JKS");

        ks.load(new FileInputStream(key),keyStorePass);


        KeyManagerFactory kmf= KeyManagerFactory.getInstance("SunX509");

        kmf.init(ks,keyPassword);

        SSLContext sslContext= SSLContext.getInstance("SSLv3");

        sslContext.init(kmf.getKeyManagers(),null,null);


        SSLServerSocketFactory factory=sslContext.getServerSocketFactory();

        s=(SSLServerSocket)factory.createServerSocket(thePort);

    }catch(Exception e)
    {
        System.out.println(e);
    }
    return(s);
}

public void run() {
    try {
        while (true) {
            SSLServerSocket sslserversocket = getServerSocket(**9991**);
            SSLSocket  client = (SSLSocket)sslserversocket.accept();
            socketList.add(client);
            new Thread(new SSocket(client,socketList,FileList)).start();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
