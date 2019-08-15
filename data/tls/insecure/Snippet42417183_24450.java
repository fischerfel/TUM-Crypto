import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class Server {

    protected final static String keyPath = "/home/*****************/trust/keystore.keystore";

    public static void main(String[] args) throws Exception{
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");

        keyStore.load(new FileInputStream(keyPath), "*****************".toCharArray());
        keyManagerFactory.init(keyStore, "*****************".toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, null);

        SSLServerSocketFactory factory = context.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(00000);

        serverSocket.setEnabledProtocols(new String[] {"SSLv3", "TLSv1"});

        SSLSocket socket = (SSLSocket) serverSocket.accept();
        socket.setTcpNoDelay(true);
        socket.setReceiveBufferSize(1024*1024*5);
        socket.setSendBufferSize(1024*1024*5);

        socket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
            @Override
            public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
                System.out.println("CipherSuite: " + handshakeCompletedEvent.getCipherSuite());
                System.out.println("CipherSuite: " + handshakeCompletedEvent.getSession().getCipherSuite());
                System.out.println("Protocol: " + handshakeCompletedEvent.getSession().getProtocol());
            }
        });

        int size = 16*1024;
        byte[] buf = new byte[size];
        InputStream in = socket.getInputStream();

        int count;
        long sum = 0;
        long startTime = System.currentTimeMillis();
        while ((count = in.read(buf)) > -1) {
            sum += count;
        }
        System.out.println();
        long stopTime = System.currentTimeMillis();

        long elapsedTime = stopTime - startTime;
        System.out.println("Size:\t" + size + "\tTime:\t" + elapsedTime + "\tRead:\t" + sum/1024/1024);
    }
}
