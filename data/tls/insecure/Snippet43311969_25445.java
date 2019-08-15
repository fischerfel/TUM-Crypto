public class Server {
public static void main(String[] arg) {

    Employee employee = null;

    try {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("simplechat.jks"), "simplechat".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "firstKey".toCharArray());
        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(ks);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

SSLServerSocketFactory.getDefault()).createServerSocket(8000);
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8000);
        System.out.println("Server Waiting");
        ObjectInputStream serverInputStream = new ObjectInputStream(c.getInputStream());
        ObjectOutputStream serverOutputStream = new ObjectOutputStream(c.getOutputStream());
    System.out.println("Connection Accepted");
        employee.setEmployeeNumber(256);
        employee.setEmployeeName("John");
        serverInputStream.close();
        serverOutputStream.close();
        System.out.println("Connection Terminated");
    } catch (Exception e) {
        System.out.println(e);
    }
}
