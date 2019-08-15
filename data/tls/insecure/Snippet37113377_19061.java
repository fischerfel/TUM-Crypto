package com.foo.foo.queue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.foo.foo.Constants.Constants;
import com.foo.foo.core.ConfigurationContainer;
import com.foo.foo.policyfinders.PolicyFinder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class BrokerThreadHLConsumer extends Thread {

private static BrokerThreadHLConsumer instance;

private static final Logger log = LogManager.getLogger(BrokerThreadHLConsumer.class);

private Channel channel;
private String queueName;
private PolicyFinder PolicyFinder;
private Connection connection;
private QueueingConsumer consumer;

private boolean loop;

private BrokerThreadHLConsumer() throws IOException {
    ConnectionFactory factory = new ConnectionFactory();
    char[] keyPassphrase = "clientrabbit".toCharArray();
    KeyStore keyStoreCacerts;
    ConfigurationContainer configurationContainer = ConfigurationContainer.getInstance();
    String exchangeName = configurationContainer.getProperty(Constants.EXCHANGE_NAME);
    String rabbitHost = configurationContainer.getProperty(Constants.RABBITMQ_SERVER_HOST_VALUE);
    try {
        /* Public key cacerts to connect to message queue*/
        keyStoreCacerts = KeyStore.getInstance("PKCS12");
        URL resourcePublicKey = this.getClass().getClassLoader().getResource("certs/client.keycert.p12");
        File filePublicKey = new File(resourcePublicKey.toURI());
        keyStoreCacerts.load(new FileInputStream(filePublicKey), keyPassphrase);
        KeyManagerFactory keyManager;

        keyManager = KeyManagerFactory.getInstance("SunX509");
        keyManager.init(keyStoreCacerts, keyPassphrase);

        char[] trustPassphrase = "changeit".toCharArray();
        KeyStore tks;

        tks = KeyStore.getInstance("JCEKS");

        URL resourceCacerts = this.getClass().getClassLoader().getResource("certs/cacerts");
        File fileCacerts = new File(resourceCacerts.toURI());

        tks.load(new FileInputStream(fileCacerts), trustPassphrase);

        TrustManagerFactory tmf;
        tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);

        SSLContext c = SSLContext.getInstance("TLSv1.1");
        c.init(keyManager.getKeyManagers(), tmf.getTrustManagers(), null);

        factory.setUri(rabbitHost);
        factory.useSslProtocol(c);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "fanout");
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        e.printStackTrace();
    } catch (KeyManagementException e1) {
        e1.printStackTrace();
    } catch (Exception e) {
        log.error("Couldn't instantiate a channel with the broker installed in " + rabbitHost);
        log.error(e.getStackTrace());
        e.printStackTrace();
    }
}

public static BrokerThreadHLConsumer getInstance() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
    if (instance == null)
        instance = new BrokerThreadHLConsumer();
    return instance;
}

public void run() {
    if (PolicyFinder != null) {
        try {
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);
            log.info("Consumer broker started and waiting for messages");
            loop = true;
            while (loop) {
                try {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    String message = new String(delivery.getBody());
                    JSONObject obj = new JSONObject(message);
                    log.info("Message received from broker " + obj);
                    if (StringUtils.isNotEmpty(message) && !PolicyFinder.managePolicySet(obj)) {
                        log.error("PolicySet error: error upgrading the policySet");
                    }
                } catch (Exception e) {
                    log.error("Receiving message error");
                    log.error(e);
                }
            }
        } catch (IOException e) {
            log.error("Consumer couldn't start");
            log.error(e.getStackTrace());
        }
    } else {
        log.error("Consumer couldn't start cause of PolicyFinder is null");
    }
}

public void close() {
    loop = false;
    try {
        consumer.getChannel().basicCancel(consumer.getConsumerTag());
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        channel.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        connection.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void setLuxPolicyFinder(PolicyFinder PolicyFinder) {
    this.PolicyFinder = PolicyFinder;
}
}
