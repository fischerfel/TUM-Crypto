    package com.test.mqilynx;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
if (android.os.Build.VERSION.SDK_INT > 9) {
 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
   StrictMode.setThreadPolicy(policy);
}
Button loginb = (Button) findViewById(R.id.button1);
loginb.setOnClickListener( new OnClickListener() {

@Override
public void onClick(View v) {
// TODO Auto-generated method stub
EditText uname = (EditText) findViewById(R.id.editText1);
EditText pwd = (EditText) findViewById(R.id.editText2);
String ustr = uname.getText().toString();
        String pstr = pwd.getText().toString();
        if (ustr.isEmpty() && pstr.isEmpty()){
            Toast to = Toast.makeText(MainActivity.this,"Please Login credentials",Toast.LENGTH_SHORT);
            to.setGravity(Gravity.CENTER, 0, 0);
            to.show();
        }
        else
        { if (ustr.isEmpty() || pstr.isEmpty())
            {
             if(ustr.isEmpty()){
             Toast to = Toast.makeText(MainActivity.this,"Please Enter Username",Toast.LENGTH_SHORT);
            to.setGravity(Gravity.CENTER, 0, 0);
            to.show();}
            else{
                Toast to = Toast.makeText(MainActivity.this,"Please Enter Password",Toast.LENGTH_SHORT);
                to.setGravity(Gravity.CENTER, 0, 0);
                to.show();
            }
                }
             else 
             {
                // Toast.makeText(getBaseContext(),"Welcome! " + ustr + " Pwd: "+ pstr,Toast.LENGTH_SHORT).show();


                 String METHOD_NAME = "Login";
                 String NAMESPACE = "https://www.qilynx.com/loadsoap.php";
                 String SOAP_ACTION = "https://www.qilynx.com/loadsoap.php/Login";
                // String URL = "https://auth:1qaz2wsx3edc4rfv@www.qilynx.com/loadsoap.php?desc";
                 String URL = "https://www.qilynx.com:443/loadsoap.php";
                 SoapObject userReq = new SoapObject(NAMESPACE,METHOD_NAME);
                 userReq.addProperty("user",ustr);
                 userReq.addProperty("pass",pstr);
                 System.out.println("user: " + "" + ustr + ":" + "Pwd: " + pstr);

            //   SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            //   request.addProperty("Login", userReq);

                 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                // envelope.bodyOut = request;
                 envelope.dotNet=true;
                 envelope.setOutputSoapObject(userReq);
                 Log.e("Value of Req", userReq.toString());
                 Log.e("Value of Envelope", envelope.toString());
                // Log.i("bodyenv", envelope.bodyOut.toString());

                 HttpTransportSE androidHTTPTransport = new HttpTransportSE(URL);

                 //By-passing certificate - START
                // Create a trust manager that does not validate certificate chains



                 TrustManager[] trustAllCerts = new TrustManager[]{
                     new X509TrustManager() {
                         public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                             return null;
                         }
                         public void checkClientTrusted(
                             java.security.cert.X509Certificate[] certs, String authType) {
                         }
                         public void checkServerTrusted(
                             java.security.cert.X509Certificate[] certs, String authType)  {
                         }
                     }
                 };

                 // Install the all-trusting trust manager
                 try {
                     SSLContext sc = SSLContext.getInstance("SSL");
                     sc.init(null, trustAllCerts, new java.security.SecureRandom());
                     HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }


                 // By-passing certificate - END
                 try{
                     System.out.println("I AM IN TRY");
                     System.out.println("envelope1 "+envelope);
                     androidHTTPTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                     System.out.println("Before Bp1");
                     System.out.println("Bp 1");
                     androidHTTPTransport.call(SOAP_ACTION,envelope);
                     System.out.println("after Bp1");
                     System.out.println("Bp 2");
                     SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                        Log.v("TAG",String.valueOf(resultsRequestSOAP));

                     System.out.println("envelope2 "+envelope); 
                     System.out.println("before resp");
                     SoapObject resp = (SoapObject) envelope.getResponse();
                     Log.i("return", resp.toString());
                     boolean check = Boolean.parseBoolean(resp.getProperty("result").toString());
                     if(check)
                     {
                         System.out.println("SUCCESS LOGIN");
                         /*Toast to = Toast.makeText(MainActivity.this,"Logged In",Toast.LENGTH_SHORT);
                            to.setGravity(Gravity.CENTER, 0, 0);
                            to.show(); */
                     }

                     else
                     {
                         /*Toast to = Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT);
                            to.setGravity(Gravity.CENTER, 0, 0);
                            to.show();*/
                         System.out.println("LOGIN FAILED");
                     }

                 }

                 catch(Exception e)
                 {
                     System.out.println("I AM IN ERROR");
                    e.printStackTrace(); 
                 }
             }
        }
        }
    });

}
