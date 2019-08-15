public class PaswordencodingActivity extends Activity 
   {
 private static final String soap_action = "http://tempuri.org/HashCode";
 private static final String method_name = "HashCode";
 private static final String namespace2 = "http://tempuri.org/";
 private static final String url2 = "http://10.0.2.2/checkhash/Service1.asmx"; 

String password="abc";
public final static int NO_OPTIONS = 0;
String hash;
    String result2;

   @Override
      public void onCreate(Bundle savedInstanceState) {
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
           } catch (NoSuchAlgorithmException e) {   
    // TODO Auto-generated catch block
    e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
    // TODO Auto-generated catch block                                          
               e.printStackTrace();
    } catch (IOException e)
              {
    // TODO Auto-generated catch block
        e.printStackTrace();
        }
            }
            else{
    Toast.makeText(PaswordencodingActivity.this, "this is a negative onClick", Toast.LENGTH_LONG).show();
            }

         }
    });

}
private static String convertToHex(byte[] bytes) throws java.io.IOException 
 {       
      StringBuffer sb = new StringBuffer();
      String hex=null;
        for (int i = 0; i < bytes.length; i++) 
        {           
          hex=Base64.encodeToString(bytes, 0, bytes.length, NO_OPTIONS);
            if (hex.length() == 1) 
            {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


 public  void SHA1(String text) 
      throws NoSuchAlgorithmException, IOException  
   { 
       MessageDigest md;
       md = MessageDigest.getInstance("SHA-1");
       byte[] sha1hash = new byte[40];
       md.update(text.getBytes("iso-8859-1"), 0, text.length());
       sha1hash = md.digest();
       hash=convertToHex(sha1hash);
       System.out.println("hash value is"+hash);
  try 
  {
        result2 = call3(hash);
} catch (XmlPullParserException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
 }

if(result2.equalsIgnoreCase(hash))
{
    System.out.println("success");

}

} 

public String call3(String hash) throws XmlPullParserException
    {
        String b="";     
        SoapObject request = new SoapObject(namespace2, method_name);                       
        request.addProperty("str",hash);
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
