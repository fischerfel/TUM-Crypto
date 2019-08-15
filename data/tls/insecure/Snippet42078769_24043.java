   import java.io.BufferedReader;
   import java.io.IOException;
   import java.io.InputStreamReader;
   import java.io.Serializable;
   import java.net.HttpURLConnection;
   import java.net.MalformedURLException;
   import java.net.URL;
   import java.security.KeyManagementException;
   import java.security.NoSuchAlgorithmException;
   import java.security.SecureRandom;
   import java.security.cert.X509Certificate;
   import java.util.Iterator;
   import java.util.List;

   import javax.net.ssl.HttpsURLConnection;
   import javax.net.ssl.SSLContext;
   import javax.net.ssl.TrustManager;
   import javax.net.ssl.X509TrustManager;

   import com.google.gson.Gson;

   import scala.Tuple2;

   import org.apache.spark.sql.SQLContext;
   import org.apache.spark.streaming.Duration;
   import org.apache.spark.streaming.api.java.JavaDStream;
   import org.apache.spark.streaming.api.java.JavaStreamingContext;
   import org.apache.spark.SparkConf;
   import org.apache.spark.SparkContext;
   import org.apache.spark.api.java.JavaRDD;
   import org.apache.spark.api.java.JavaSparkContext;
   import org.apache.spark.api.java.function.Function;
   import org.apache.spark.sql.DataFrame;

   public class Connection implements Serializable{

/**
 * 
 */
private static final long serialVersionUID = 1L;
private static String output;
public static BusStopData bsd;
public static List<BusArrivalData> cd; 



public static void responseConnection() throws NoSuchAlgorithmException, KeyManagementException {
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
    }};

     try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url = new URL("https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation?stopid=45&routeid=1&format=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);

                bsd = new Gson().fromJson(output, BusStopData.class);
                cd = bsd.getResults();  
            }

            conn.disconnect();

            System.out.println("Bus Stop ID: " + bsd.getStopid());
            System.out.println("No of Results: " + bsd.getNumberofresults());
            System.out.println("TimeStamp: " + bsd.getTimestamp());
            System.out.println("Results Size: " + cd.size());
            System.out.println(bsd.getResults().iterator().next().getScheduledarrivaldatetime());

            for (Iterator iterator = cd.iterator(); iterator.hasNext();) {
                BusArrivalData busArrivalData = (BusArrivalData) iterator.next();   
                System.out.println("Departure Times: " +busArrivalData.getDeparturedatetime());             
            }

            //=========TODO============


          } catch (MalformedURLException e) {

            e.printStackTrace();

          } catch (IOException e) {

            e.printStackTrace();

          }

     SparkConf sparkConf = new SparkConf()
             .setAppName("Example")
             .setMaster("local[*]");

     JavaSparkContext sc = new JavaSparkContext(sparkConf);
     SQLContext sqlContext = new SQLContext(sc);

     DataFrame df = sqlContext.read().json(output);
     df.select(
                df.col("stopid"),
                df.col("timestamp"),
                org.apache.spark.sql.functions.explode(df.col("results")).as("results")
            );
     df.show();

}
