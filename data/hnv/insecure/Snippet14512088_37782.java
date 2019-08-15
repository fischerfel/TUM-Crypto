public class TwitterUpdater {
     Context context;
     public TwitterUpdate(Context ctx){
        context = ctx;
        Timer timer = new Timer;
        timer.scheduleAtFixedRate(new TwitterTask(), 0, 300000);
     }

}

public class TwitterTask extends TimerTask{
    @Override
    public void run() {
        // TODO Auto-generated method stub
        this.loadMessages();
    }

}
public void loadMessages(){
        StringBuilder builder = new StringBuilder();
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


        HttpGet httpGet = new HttpGet("https://api.twitter.com/1/statuses/user_timeline.json?include_entities=true&include_rts=true&screen_name=<Your twitter account name>");
        try {
          HttpResponse response = httpClient.execute(httpGet);
          StatusLine statusLine = response.getStatusLine();
          int statusCode = statusLine.getStatusCode();
          if (statusCode == 200) {

            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
              builder.append(line);
            }
            JSONArray obj = new JSONArray(builder.toString());
            if (obj.getClass() == JSONArray.class){
                JSONArray  results = obj;
                for(int i = 0; i < results.length(); i++){
                    JSONObject result = results.getJSONObject(i);

                    JSONObject user = result.getJSONObject("user");
                    if(((String) user.get("screen_name")).compareToIgnoreCase("TNCVB")== 0){
                        String name = user.getString("screen_name");
                        String text =result.getString("text");
                        String timestamp = result.getString("created_at");
                        //Put them in an object and add to array or something
                    }
                }
            }

                Intent nintent = new Intent(context, MyActivity.class);
                PendingIntent pintent = PendingIntent.getActivity(context, 0, nintent, 0);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Update")
                        .setContentIntent(pintent)
                        .setContentText("New messages are available.");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(007, mBuilder.build());
          } else {
            Log.e("JSON Error:", "Failed to download file");
          }
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }catch(Exception e){

        }

    }
}
