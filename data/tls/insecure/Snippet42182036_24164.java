import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;

import com.google.common.io.Closeables;
import com.google.gson.Gson;

public class JavaCustomReceiver extends Receiver<String>{


private static final long serialVersionUID = 1L;
Map<String, ArrayList<String>> dataMap = new HashMap<String,        ArrayList<String>>();
ArrayList<String> busData = new ArrayList<String>();
private static BusStopData bsd;
private static String output;
private String host;
private int port = -1;
private SSLContext sc;
private URL url;
String urlString ="https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation?stopid=45&routeid=1&format=json";
private String actualArrivalTime;
private String delayTime;
private Date arrivaldatetime;
private Date timestamp;
private Date scheduledarrivaldatetime;


  public JavaCustomReceiver() {
    super(StorageLevel.MEMORY_AND_DISK_2());

  }

  public void onStart() {
    // Start the thread that receives data over a connection
    new Thread()  {
      @Override public void run() {
        receive();

      }
    }.start();
  }

  public void onStop() {
    // There is nothing much to do as the thread calling receive()
    // is designed to stop by itself if isStopped() returns false
  }

  /** Create a socket connection and receive data until receiver is stopped */
  @SuppressWarnings("resource")
private synchronized void receive() {

    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()   {
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
    }};


     try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

            //System.out.println("Output from Server .... \n");
            while (!isStopped() && (output = br.readLine()) != null) {
                System.out.println(output);

                store(output);              
            }

      // Restart in an attempt to connect again when server is active again
      restart("Trying to connect again");
    } catch(ConnectException ce) {
      // restart if could not connect to server
      restart("Could not connect", ce);
    } catch(Throwable t) {
      // restart if there is any other error
      restart("Error receiving data", t);
    }
  }
 }
