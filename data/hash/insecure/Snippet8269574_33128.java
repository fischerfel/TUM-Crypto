           package com.kar.encodePassword;
           import java.io.IOException;
           import java.io.UnsupportedEncodingException;
           import java.net.SocketException;
           import java.security.MessageDigest;
           import java.security.NoSuchAlgorithmException;
           import org.ksoap2.SoapEnvelope;
           import org.ksoap2.serialization.SoapObject;
           import org.ksoap2.serialization.SoapPrimitive;
           import org.ksoap2.serialization.SoapSerializationEnvelope;
           import org.ksoap2.transport.HttpTransportSE;
           import org.xmlpull.v1.XmlPullParserException;
           import android.app.Activity;
           import android.os.Bundle;
           import android.util.Base64;
           import android.util.Log;
           import android.view.View;
           import android.widget.Button;
           import android.widget.EditText;
           import android.widget.Toast;

   public class PaswordencodingActivity extends Activity {
       /** Called when the activity is first created. */

 private static final String soap_action = "http://tempuri.org/HashCode";
 private static final String method_name = "HashCode";
 private static final String namespace2 = "http://tempuri.org/";
 private static final String url2 = "http://10.0.2.2/checkhash/Service1.asmx"; 

String password="abc";
public final static int NO_OPTIONS = 0;
String hash;
    String result2;


@Override
public void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final EditText pass=(EditText)findViewById(R.id.editText1);
    Button encode=(Button)findViewById(R.id.button1);            
    encode.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v)  {
            // Perform action on click
            password=pass.getText().toString();
            if(password!=null){
                try { 
        SHA1(password) ;
        } catch (UnsupportedEncodingException e) {                                          
                      e.printStackTrace();
        } catch (IOException e) {   
            e.printStackTrace();                    
                      }
            }
            else{
       Toast.makeText(PaswordencodingActivity.this, "this is a negative onClick", Toast.LENGTH_LONG).show();
            }

           }

          });


        }


private static String convertToHex(byte[] data) throws java.io.IOException 
 {
        System.out.println("data received is"  +data);

        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex=Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

        for (int i = 0; i < data.length; i++) 
        {            
            if (hex.length() == 1) 
            {
                sb.append('0');
            }
            sb.append(hex);
        }

       return sb.toString();
    }

public void SHA1(String text) throws IOException
{
    MessageDigest mdSha1 = null;
    try 
    {
      mdSha1 = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e1) {
      Log.e("myapp", "Error initializing SHA1 message digest");
    }
    mdSha1.update(text.getBytes("iso-8859-1"), 0, text.length());
    byte[] data = mdSha1.digest();
    hash=convertToHex(data);

    System.out.println("data going is"  +data);
    System.out.println("hash value"+hash);

    try
    {
        result2=call3(password);
        if(result2.equalsIgnoreCase(hash.toString()))
        System.out.println("success");


    } catch (XmlPullParserException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }   

        }

public String call3(String pass) throws XmlPullParserException
{
        String b=""; 

        SoapObject request = new SoapObject(namespace2, method_name);      
        request.addProperty("str",pass);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
        envelope.dotNet = true; 
        envelope.setOutputSoapObject(request);

        HttpTransportSE  android = new HttpTransportSE(url2);

        android.debug = true; 
 try 
 {

        android.call(soap_action, envelope);

        SoapPrimitive result = (SoapPrimitive)envelope.getResponse();
        Log.i("myapp",result.toString());
        System.out.println(" --- response ---- " + result); 
        b=result.toString();


        } catch (SocketException ex) { 
            ex.printStackTrace(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 

        return b;   

}
}
