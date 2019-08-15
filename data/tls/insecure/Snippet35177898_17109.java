package com.cs.srp.rdp.omb;

import org.apache.bcel.classfile.ConstantClass;

 import javax.jms.*;
 import javax.naming.InitialContext;
 import javax.net.ssl.KeyManagerFactory;
 import javax.net.ssl.SSLContext;
 import javax.net.ssl.TrustManagerFactory;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.InputStream;
 import java.security.KeyStore;
 import java.util.ArrayList;
 import java.util.Hashtable;
 import java.util.List; 
 import java.util.Properties;

 import static javax.naming.Context.*;

 public class OMBTopicPublisher {


private InitialContext ic = null;
private Connection connection;
private Session session;
ConnectionFactory connectionFactory;

private Connection createConnection() throws JMSException {
    String providerCredentials = "aGMk643R";
    String providerUrl = "ldap://esd-qa.csfb.net/ou=MQ,ou=Services,dc=csfb,dc=CS-Group,dc=com";
    String keyStoreFile = "C:\\Balaji\\workspace\\workflowrda\\src\\main\\properties\\jks\\test\\keystore.jks";
    String password = "rFzv0UOS";
    String queueManagerConnectionFactory = "cn=USTCMN01_CF";
    try {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(keyStoreFile), password.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, password.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);

        Hashtable<String, String> hashTable = new Hashtable<>();
        hashTable.put(PROVIDER_URL, providerUrl);
        hashTable.put(INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        hashTable.put(SECURITY_AUTHENTICATION, "simple");
        hashTable.put(SECURITY_PRINCIPAL, "uid=MQRDP,ou=People,o=Administrators,dc=CS-Group,dc=com");
        hashTable.put(SECURITY_CREDENTIALS, providerCredentials);

        ic = new InitialContext(hashTable);
        connectionFactory = (ConnectionFactory) ic.lookup(queueManagerConnectionFactory);
        connection = connectionFactory.createConnection();
    } catch (Exception e) {
        System.out.println(e.getMessage());
        System.out.println("Exception while trying to connect to DEV OMB queue");
    }
    return connection;
}

private Session createSession() throws JMSException {
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    return session;
}

private void init() throws JMSException {
    createConnection();
    createSession();
}

public void produceWorkflowMessage(String inboundControlMessageXML) throws JMSException {
    init();
    String destinationStr = "EDM.BIRS.RDP.S1.ONEPPM.TO_RDA";
    MessageProducer producer;
    Destination destination;
    try {
        destination = (Destination) ic.lookup("cn=" + destinationStr);
        TextMessage message = session.createTextMessage(inboundControlMessageXML);
        producer = session.createProducer(destination);
        producer.send(message);
    } catch (Exception e) {
        System.out.println(e.getMessage());
        System.out.println("Exception occured while posting message to OMB topic");
        System.exit(1);
    } finally {
        session.close();
        connection.close();
    }
}

public static List<String> loadFromPropertiesFile(String propFileName) {
    InputStream inputStream = null;
    List<String> workflowMessageList = null;
    try {
        Properties workflowProperties = new Properties();
        inputStream = ConstantClass.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputStream != null) {
            workflowProperties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '"
                    + propFileName + "' not found in the classpath");
        }
        workflowMessageList = new ArrayList<String>();
        for (String key : workflowProperties.stringPropertyNames()) {
            System.out.println("Key =" + key);
            String value = workflowProperties.getProperty(key);
            workflowMessageList.add(value);
        }

    } catch (Exception e) {
        System.out.println("Error loading inboundxml worflow messages from properties file: " + e);
    } finally {
        try {
            inputStream.close();
        } catch (IOException ioe) {
            System.out
                    .println("Exception while closing the file stream");
        }
    }
    return workflowMessageList;
}
