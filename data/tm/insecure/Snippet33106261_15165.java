import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.*;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class mq1 {

public static String    host = "localhost";
public static int       port = 1414;
public static String    strchannel = "MQ.CHL";
public static String    strqueuemanager = "MQMGR";
public static String    strqueue = "REQUEST.QUEUE";

@SuppressWarnings("deprecation")
public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
//call patch() to skip SSL 
patch();

int openOptions = CMQC.MQOO_BROWSE | CMQC.MQOO_INQUIRE | CMQC.MQOO_OUTPUT | CMQC.MQOO_INPUT_AS_Q_DEF ;

MQEnvironment.hostname = host;
MQEnvironment.port = port;
MQEnvironment.channel = strchannel;
MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY,CMQC.TRANSPORT_MQSERIES);

MQQueueManager qMgr;
try {
qMgr = new MQQueueManager (strqueuemanager);
System.out.println(qMgr);
MQQueue destQueue = qMgr.accessQueue(strqueue, openOptions);
System.out.println("Queue size:" + destQueue.getCurrentDepth()); 
MQMessage hello_world = new MQMessage(); 
System.out.println("MQMessage message created");
hello_world.writeUTF("Sending Sample message"); 
MQPutMessageOptions pmo = new MQPutMessageOptions();
try {
destQueue.put(hello_world,pmo);
destQueue.get(hello_world);
}
catch (Exception e)
{
System.out.println(e.getMessage());
}

int len = hello_world.getDataLength();
System.out.println("Length : " + len);
System.out.println("GET: "+ hello_world.readString(len-1));
destQueue.close();
qMgr.disconnect();
} catch (Exception e) {
e.printStackTrace();
}
}


public static void patch() throws KeyManagementException, NoSuchAlgorithmException {

System.out.println("Calling SSL patch");

TrustManager[] trustAllCerts = new TrustManager[]{
new X509TrustManager() {
public java.security.cert.X509Certificate[] getAcceptedIssuers() {
return null;
}

public void checkClientTrusted(X509Certificate[] certs, String authType) {
}

public void checkServerTrusted(X509Certificate[] certs, String authType) {
}

}
};

System.out.println("trustAllCerts = "+trustAllCerts);

SSLContext sc = SSLContext.getInstance("SSL");

System.out.println("sc before init = "+sc);

sc.init(
null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HostnameVerifier allHostsValid = new HostnameVerifier() {
public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
return true;
}
};

System.out.println("sc after init= "+sc);
System.out.println("allHostsValid= "+allHostsValid);
// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
}

}
