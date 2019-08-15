    package com.example.test_upload;

    import java.io.DataOutputStream;
    import java.io.IOException;
    import java.net.CookieHandler;
    import java.net.CookieManager;
    import java.net.MalformedURLException;
    import java.net.URL;
    import java.security.KeyManagementException;
    import java.security.NoSuchAlgorithmException;
    import java.security.cert.X509Certificate;

    import javax.net.ssl.HostnameVerifier;
    import javax.net.ssl.HttpsURLConnection;
    import javax.net.ssl.SSLContext;
    import javax.net.ssl.SSLSession;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;

    import android.annotation.SuppressLint;
    import android.app.Activity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.widget.TextView;


    public class Test_upload extends Activity {

private static final String DEBUG_TAG = null;

@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_upload);

    String TAG = "TAG";
    String id="5027300";
    String password = "PlSoUrXu";
    String filename="00000221000300015120128140924.dat";
    String file="abcdtesttest";

    //set parameters
    String urlParameters = "_id="+id+"&_password="+password+"&_filename="+filename+"&_base64file="+file;        



        try {
                // Set your server page url
                URL connectURL = new URL("https:url of my server");

                Log.e(TAG,"Starting Http File Sending to URL");

                // Open a HTTP connection to the URL
                HttpsURLConnection conn = (HttpsURLConnection)connectURL.openConnection();

                //cookie set
                //CookieManager cookieManager = new CookieManager();  
                //CookieHandler.setDefault(cookieManager);

                // Alow Inputs
                conn.setDoInput(true);

                // Allow Outputs
                conn.setDoOutput(true);

                // Don't use a cached copy.
                conn.setUseCaches(false);

                // Use a post method.
                conn.setRequestMethod("POST");

                //********** Create a trust manager that does not validate certificate chains
                   /*TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    } };
                    // Install the all-trusting trust manager
                    final SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    // Create all-trusting host name verifier
                    HostnameVerifier allHostsValid = new HostnameVerifier(){
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            // TODO Auto-generated method stub
                            return true;
                        }
                    };

                    // Install the all-trusting host verifier
                    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);*/

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
                conn.setRequestProperty("Content-Length", ""+ Integer.toString(urlParameters.getBytes().length));

                //Open output stream
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(urlParameters);

                Log.e(TAG,"Headers are written");

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                //Log.e(TAG,serverResponseCode);
                Log.d(DEBUG_TAG, "Server responce code: " + serverResponseCode);
                Log.e(TAG,"Server responce msg:"+serverResponseMessage);

                /*Find the view by its id
                TextView tv1 = (TextView)findViewById(R.id.textView3);*/
                TextView tv2 = (TextView)findViewById(R.id.textView4);



                //Set the text
                //tv1.setText(serverResponseCode);
                tv2.setText(serverResponseMessage);

                dos.flush();
                dos.close();
                conn.disconnect();





        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }










}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_test_upload, menu);
    return true;
}

}
