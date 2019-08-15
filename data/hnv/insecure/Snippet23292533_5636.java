    public class MakeRoti extends Activity{
private SeekBar thickness; 
private SeekBar roast; 
private SeekBar oil; 
private EditText numbers;

int thickness_val;
int roast_val;
int oil_val;

String no_rotis;


String thick;
String oilval;
String roastval;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.makeroti);
    thickness= (SeekBar)findViewById(R.id.thickness);
    thickness.setProgress(1);
    thickness.incrementProgressBy(1);
    thickness.setMax(5);
    roast= (SeekBar)findViewById(R.id.roast);
    roast.setProgress(1);
    roast.incrementProgressBy(1);
    roast.setMax(5);
    oil= (SeekBar)findViewById(R.id.oil);
    oil.setProgress(1);
    oil.incrementProgressBy(1);
    oil.setMax(3);
    numbers=(EditText)findViewById(R.id.numberval);

}
public void sendData(View view) 
{

    thickness_val=thickness.getProgress();;
     roast_val=roast.getProgress();
     oil_val=oil.getProgress();
     no_rotis=numbers.getText().toString();

     thick=Integer.toString(thickness_val);
    roastval=Integer.toString(roast_val);
     oilval=Integer.toString(oil_val);


            new LongOperation().execute();


}
private class LongOperation extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
nameValuePairs.add(new BasicNameValuePair("thickness", thick));
nameValuePairs.add(new BasicNameValuePair("roastval", roastval));
nameValuePairs.add(new BasicNameValuePair("oilval", oilval));
nameValuePairs.add(new BasicNameValuePair("number", no_rotis));



    String url="https://192.168.0.100/testhttps.php";
    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

    DefaultHttpClient client = new DefaultHttpClient();

    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
    registry.register(new Scheme("https", socketFactory, 443));
    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
    DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

    // Set verifier     
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

    // Example send http request

    HttpPost httpPost = new HttpPost(url);
    try {

        HttpResponse response = httpClient.execute(httpPost);
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block  
        e.printStackTrace();
    }

        return "";
    }

    protected void onPostExecute(String result) {

    }

    protected void onPreExecute() {}

    protected void onProgressUpdate(Void... values) {}
}

}
