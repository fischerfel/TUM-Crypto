public class HomeActivity extends ListActivity {

    private static final String apiKey = "67htj4455jsd345jj956id"; 
    private static final String apiUser = "android";
    private static final String ChosenTeam = null;
    private static String ChosenMethod = null; 
    public String fulldata = null;
    public String chosenLeagueId = "40";
    public List<String> newsList = null;
    public int newsAmount = 0;
    private static final String ChosenMethodPref = null;





    long unixTimeStamp = System.currentTimeMillis() / 1000L;

    //add Time Stamp to URL


    //public String chosenMethod; 


    String newsFeedRequest = "1.0/website/" + chosenLeagueId + "/news?timestamp=" + unixTimeStamp;
    String fixturesFeedURL = "https://www.example.com/_services/api/" + newsFeedRequest;


    private static String buildHmacSignature(String pKey, String pStringToSign)
    {
      String lSignature = "None";
      try
      {
        Mac lMac = Mac.getInstance("HmacSHA256");
        SecretKeySpec lSecret = new SecretKeySpec(pKey.getBytes(), "HmacSHA256");
        lMac.init(lSecret);

        byte[] lDigest = lMac.doFinal(pStringToSign.getBytes());
        BigInteger lHash = new BigInteger(1, lDigest);
        lSignature = lHash.toString(16);
        if ((lSignature.length() % 2) != 0) {
          lSignature = "0" + lSignature;
        }
      }
      catch (NoSuchAlgorithmException lEx)
      {
        throw new RuntimeException("Problems calculating HMAC", lEx);
      }
      catch (InvalidKeyException lEx)
      {
        throw new RuntimeException("Problems calculating HMAC", lEx);
      }

      return lSignature;
    }


    public void checkPreferences(){


        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        final String ChosenMethodPref = preferences.getString("ChosenMethod", ChosenMethod);
        Log.v("myapp", "ChosenMethod Home = " + ChosenMethodPref);

         if (ChosenMethodPref.equals("Team")) {
             setContentView(R.layout.homeactteam);
             newsAmount = 5;
             TextView redHeaderText = (TextView) findViewById(R.id.redheadertext);
             redHeaderText.setText("Latest League Fixture");



         } else {
             setContentView(R.layout.homeact);
             newsAmount = 10;

         }

}
    public void loadData(){String myhash = buildHmacSignature(apiKey, fixturesFeedURL);





    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(fixturesFeedURL);

    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    pairs.add(new BasicNameValuePair("requestToken", myhash));
    pairs.add(new BasicNameValuePair("apiUser", apiUser));

    try {
        post.setEntity (new UrlEncodedFormEntity(pairs));
        HttpResponse response = client.execute(post);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        fulldata = String.valueOf(json);
        Log.v("myApp","newsdata" + fulldata);
        newsList = new ArrayList<String>();
        JSONObject obj = new JSONObject(json);    
        JSONObject objData = obj.getJSONObject("data");
        JSONArray jArray = objData.getJSONArray("news");


           for(int t = 0; t < newsAmount; t++){
               JSONObject newsTitleDict = jArray.getJSONObject(t);


               newsList.add(newsTitleDict.getString("title"));

           }


    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


    setListAdapter ( new ArrayAdapter<String>(this, R.layout.single_item, newsList));

       ListView list = getListView();

        list.setTextFilterEnabled(true);



    }   


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        checkPreferences();
        loadData();


        Button backbtn = (Button) findViewById(R.id.backbtn);

        //Listening to button event
        backbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent previousScreen = new Intent(getApplicationContext(), ChooseTeamActivity.class);
                ChosenMethod = "null";
                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ChosenMethod", ChosenMethod);            
                editor.commit();
                previousScreen.putExtra("FullData", fulldata);
                startActivity(previousScreen);


            }
        });




    }











@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == R.id.home) {

        startActivity(new Intent(HomeActivity.this, HomeActivity.class));

        return(true);
  }


    if (item.getItemId() == R.id.match) {

        startActivity(new Intent(HomeActivity.this, MatchActivity.class));

        return(true);
  }



    if (item.getItemId() == R.id.teams) {

        startActivity(new Intent(HomeActivity.this, TeamsActivity.class));

        return(true);
  }



    if (item.getItemId() == R.id.twitter) {

        startActivity(new Intent(HomeActivity.this, TwitterActivity.class));

        return(true);
  }

    if (item.getItemId() == R.id.info) {

        startActivity(new Intent(HomeActivity.this, InfoActivity.class));

        return(true);
  }


    return(super.onOptionsItemSelected(item));


}



}
