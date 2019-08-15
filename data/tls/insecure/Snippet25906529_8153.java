import com.ibm.mq.jms.*;

import java.io.FileInputStream;
import java.io.Console;
import java.security.*;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.ibm.mq.jms.MQQueueConnectionFactory;

public class SSLTest {

   public static void main(String[] args) {
      System.out.println(System.getProperty("java.home"));

      String HOSTNAME = "myhost";
      String QMGRNAME = "MyQMGR";
      String CHANNEL = "MY.SVRCONN";
      String SSLCIPHERSUITE = "TLS_RSA_WITH_AES_256_CBC_SHA";

      try {
         Class.forName("com.sun.net.ssl.internal.ssl.Provider");

         System.out.println("JSSE is installed correctly!");

         Console console = System.console();
         char[] KSPW = console.readPassword("Enter keystore password: ");

         // instantiate a KeyStore with type JKS
         KeyStore ks = KeyStore.getInstance("JKS");
         // load the contents of the KeyStore
         ks.load(new FileInputStream("/home/hudo/hugo.jks"), KSPW);
         System.out.println("Number of keys on JKS: "
               + Integer.toString(ks.size()));

         // Create a keystore object for the truststore
         KeyStore trustStore = KeyStore.getInstance("JKS");
         // Open our file and read the truststore (no password)
         trustStore.load(new FileInputStream("/home/xwgztu2/xwgztu2.jks"), null);

         // Create a default trust and key manager
         TrustManagerFactory trustManagerFactory =
           TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
         KeyManagerFactory keyManagerFactory =
           KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

         // Initialise the managers
         trustManagerFactory.init(trustStore);
         keyManagerFactory.init(ks,KSPW);

         // Get an SSL context.
         // Note: not all providers support all CipherSuites. But the
         // "SSL_RSA_WITH_3DES_EDE_CBC_SHA" CipherSuite is supported on both SunJSSE
         // and IBMJSSE2 providers

         // Accessing available algorithm/protocol in the SunJSSE provider
         // see http://java.sun.com/javase/6/docs/technotes/guides/security/SunProviders.html
         SSLContext sslContext = SSLContext.getInstance("SSLv3");

         // Acessing available algorithm/protocol in the IBMJSSE2 provider
         // see http://www.ibm.com/developerworks/java/jdk/security/142/secguides/jsse2docs/JSSE2RefGuide.html
         // SSLContext sslContext = SSLContext.getInstance("SSL_TLS");
          System.out.println("SSLContext provider: " +
                            sslContext.getProvider().toString());

         // Initialise our SSL context from the key/trust managers
         sslContext.init(keyManagerFactory.getKeyManagers(),
                         trustManagerFactory.getTrustManagers(), null);

         // Get an SSLSocketFactory to pass to WMQ
         SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

         // Create default MQ connection factory
         MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

         // Customize the factory
         factory.setSSLSocketFactory(sslSocketFactory);
         // Use javac SSLTest.java -Xlint:deprecation
         factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
         factory.setQueueManager(QMGRNAME);
         factory.setHostName(HOSTNAME);
         factory.setChannel(CHANNEL);
         factory.setPort(1414);
         factory.setSSLFipsRequired(false);
         factory.setSSLCipherSuite(SSLCIPHERSUITE);

         QueueConnection connection = null;
         connection = factory.createQueueConnection("",""); //empty user, pass to avoid MQJMS2013 messages
         connection.start();
         System.out.println("JMS SSL client connection started!");
         connection.close();

      } catch (JMSException ex) {
         ex.printStackTrace();
      } catch (Exception ex){
         ex.printStackTrace();
      }
   }
}
