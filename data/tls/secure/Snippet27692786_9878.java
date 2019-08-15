package com.rabbitmq.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultSaslConfig;
import com.rabbitmq.client.QueueingConsumer;

public class CertificateAuthenticatedRabbitMQClientExample {

    private static final String CLIENT_CERTIFICATE_PASSWORD = "MySecretPassword";
    private static final String QUEUE_USED = "sampleQueue";
    public static void main(String[] args) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, KeyManagementException, InterruptedException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        KeyManager[] clientKeyManagerList = null;
        try(FileInputStream clientCertificateInputStream = new FileInputStream(new File("/home/developer/rabbitmqcert/ruleprocessing.password.p12"))) {
            KeyStore clientKeStore = KeyStore.getInstance("PKCS12");                                        //Create a clean KeyStore
            clientKeStore.load(clientCertificateInputStream, CLIENT_CERTIFICATE_PASSWORD.toCharArray());    //Load the client's certificate into the keystore
            KeyManagerFactory clientSSLKeyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            clientSSLKeyManagerFactory.init(clientKeStore, CLIENT_CERTIFICATE_PASSWORD.toCharArray());
            clientKeyManagerList = clientSSLKeyManagerFactory.getKeyManagers();                             //Get list of key managers (in essence, only the keystore with the client certificate)
        }
        TrustManager[] clientTrustManagerList = {
          new X509TrustManager() {
              //Dummy trust store that trusts any server you connect to.
              //For demo purposes only
              @Override
              public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
              @Override
              public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
              @Override
              public X509Certificate[] getAcceptedIssuers() { return null;}
          }
        };
        sslContext.init(clientKeyManagerList, clientTrustManagerList, null);        //Initialize SSL context with the key and trust managers we've created/loaded before

        ConnectionFactory rabbitMqConnectionFactory = new ConnectionFactory();      //Create factory
        rabbitMqConnectionFactory.setHost("localhost");
        rabbitMqConnectionFactory.setPort(5671);
        rabbitMqConnectionFactory.setSaslConfig(DefaultSaslConfig.EXTERNAL);        //Set authentication method as SSL auth
        rabbitMqConnectionFactory.useSslProtocol(sslContext);                       //Set the created SSL context as the one to use
        Connection rabbitMqOutboundConnection = null;
        Channel rabbitMqOutboundChannel = null;
        try {
            rabbitMqOutboundConnection = rabbitMqConnectionFactory.newConnection();
            rabbitMqOutboundChannel = rabbitMqOutboundConnection.createChannel();
            rabbitMqOutboundChannel.queueDeclare(QUEUE_USED, false, false, false, null);
            rabbitMqOutboundChannel.basicPublish("", QUEUE_USED, null, "This is a sample message".getBytes());
            System.out.println("Message successfully sent to queue");
        }finally{
            if(rabbitMqOutboundChannel != null) {
                rabbitMqOutboundChannel.close();
            }
            if(rabbitMqOutboundConnection != null) {
                rabbitMqOutboundConnection.close();
            }
        }
        Connection rabbitMqInboundConnection = null;
        Channel rabbitMqInboundChannel = null;
        try {
            rabbitMqInboundConnection = rabbitMqConnectionFactory.newConnection();
            rabbitMqInboundChannel = rabbitMqInboundConnection.createChannel();
            rabbitMqInboundChannel.queueDeclare(QUEUE_USED, false, false, false, null);
            QueueingConsumer rabbitMqQueueConsumer = new QueueingConsumer(rabbitMqInboundChannel);
            rabbitMqInboundChannel.basicConsume(QUEUE_USED, true, rabbitMqQueueConsumer);
            QueueingConsumer.Delivery deliveryResult = rabbitMqQueueConsumer.nextDelivery();
            System.out.println("Message read from the queue: " + new String(deliveryResult.getBody()));
        }finally{
            if(rabbitMqInboundChannel != null) {
                rabbitMqInboundChannel.close();
            }
            if(rabbitMqInboundConnection != null) {
                rabbitMqInboundConnection.close();
            }
        }
    }

}
