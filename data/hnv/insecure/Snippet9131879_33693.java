public abstract class BaseAsyncWorker extends AsyncTask<String, Void, String>{
public static final String AS = "BaseAsyncWorker";
private  String URL;
private String result;
final Context context;

public BaseAsyncWorker(String url,Context context){
    this.URL = url; 
    this.context = context;
}
//before
@Override
protected abstract void onPreExecute();

//background
@Override
protected  String doInBackground(String... objects) {
    for (String obj : objects) {
        Log.d(AS,obj.toString() );
        Log.d(AS,"beginning background" );
        Logger.appendLog("Start response...");
        try{


            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier)hostnameVerifier);
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", socketFactory,443));
            SingleClientConnManager mngr = new SingleClientConnManager(client.getParams(),
                                                                        registry);
            //trustEveryone();
            DefaultHttpClient httpClient = new DefaultHttpClient(mngr,client.getParams());

            //MMGHttpClient httpClient = new MMGHttpClient(context);
            //httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "MyMobiGift Ltd. Android");
            //HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

            HttpPost httpPost = new HttpPost(URL); 

            StringEntity se = new StringEntity(obj);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");



            HttpResponse response = (HttpResponse)httpClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            if((status.getStatusCode())==200){
                HttpEntity entity = response.getEntity();
                if(entity!=null){
                    InputStream instream = entity.getContent();
                    result= convertStreamToString(instream);
                    instream.close();
                    Logger.appendLog("End response with result: "+result);
                }else{
                    result=null;
                    Logger.appendLog("End response without result");
                }
            }
        }catch (ClientProtocolException e) {Logger.appendLog("ClientProtocolException at"+e.getMessage());}
        catch (IOException e) {Logger.appendLog("IOException at" + e.getMessage());}

    }   
    return result;  
}
