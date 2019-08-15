import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import co.uk.fantasticmedia.TheEvoStikLeague.PullToRefreshListView.OnRefreshListener;

import com.commonsware.cwac.merge.MergeAdapter;


public class HomeActivity extends ListActivity {

    private MergeAdapter adapter=null;
    private ArrayAdapter<String> arrayAdapter=null;
    private ArrayAdapter<String> arrayAdapter2=null;
    private LazyAdapter arrayAdapter3=null;
    private static final String apiKey = ""; 
    private static final String apiUser = "";
    private static String ChosenLeagueID = null;
    private static String ChosenMethod = null; 
    private static String ChosenTeamId = null; 
    public String fulldata = null;
    public String newsFeedRequest = null;
    public String newsFeedURL = null;
    public String resultsFeedURL = null;
    public String lastMonth = null;
    public String HomeTeam = null;
    public String AwayTeam = null;
    public String HomeScore = null;
    public String AwayScore = null;
    public String Attendance = null;
    public String Division = null;
    public String HomeScorers = null;
    public String AwayScorers = null;





    public String resultsFeedRequest = null;
    public String chosenLeagueId = "40";
    public List<String> newsList = null;
    public List<String> newsList2 = null;
    public List<String> newsList3 = null;
    public List<String> imageList = null;
    public JSONObject resultsDict = null;
    public View resultsView = null;



    public int newsAmount = 0;

    long unixTimeStamp = System.currentTimeMillis() / 1000L;

    //add Time Stamp to URL


    //public String chosenMethod; 


    public class PostTask extends AsyncTask<Void, String, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = false;
         checkPreferences();


            publishProgress("progress");
            return result;
        }

        protected void onProgressUpdate(String... progress) {
            StringBuilder str = new StringBuilder();
                for (int i = 1; i < progress.length; i++) {
                    str.append(progress[i] + " ");
                }

        }

    }







    static String buildHmacSignature(String pKey, String pStringToSign)
    {
      String lSignature = "None";
      try
      {
        Mac lMac = Mac.getInstance("");
        SecretKeySpec lSecret = new SecretKeySpec(pKey.getBytes(), "");
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
        ChosenMethod = preferences.getString("ChosenMethod", ChosenMethod);
        ChosenLeagueID = preferences.getString("ChosenLeagueId", ChosenLeagueID);
        ChosenTeamId = preferences.getString("ChosenTeamId", ChosenTeamId);








         Log.v("lc", "newsurl" + newsFeedURL);


        Log.v("myapp", "ChosenMethod Home = " + ChosenMethod);
        Log.v("myapp", "ChosenLeagueID Home = " + ChosenLeagueID);
        Log.v("myapp", "ChosenTeamID Home = " + ChosenTeamId);

         if (ChosenMethod.equals("Team")) {
             setContentView(R.layout.homeactteam2);
             newsAmount = 5;
             loadData();



         } else {
             setContentView(R.layout.homeact);
             newsAmount = 10;
             loadDataLeague();
         }

}


    public void loadresults(){

    resultsFeedRequest = "1.0/league-website/" + chosenLeagueId + "/results?&team_id=" + ChosenTeamId + "&limit=31&timestamp=" + unixTimeStamp;
    resultsFeedURL = "https://www.website.com/_services/api/" + resultsFeedRequest; 

    String myhash = buildHmacSignature(apiKey, resultsFeedURL);

    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(resultsFeedURL);

    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    pairs.add(new BasicNameValuePair("requestToken", myhash));
    pairs.add(new BasicNameValuePair("apiUser", apiUser));

    try {
        post.setEntity (new UrlEncodedFormEntity(pairs));
        HttpResponse response = client.execute(post);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        fulldata = String.valueOf(json);
        Log.v("myApp","resultsdata" + fulldata);

        newsList = new ArrayList<String>();
        newsList2 = new ArrayList<String>();
        newsList3 = new ArrayList<String>();
        imageList = new ArrayList<String>();


        JSONObject obj = new JSONObject(json);    
        JSONObject objData = obj.getJSONObject("data");
        JSONArray jArray = objData.getJSONArray("results");


        if(jArray.length() < 1){

            loadLastResults();

        }else{

            Log.v("lc", "this month has results");

        }


//      
//         for(int t = 0; t < newsAmount; t++){
//             JSONObject newsTitleDict = jArray.getJSONObject(t);
//             imageList.add(newsTitleDict.getString("image_small"));
//           newsList3.add(newsTitleDict.getString("title"));
//             
//         }
//      
//         for(int t = 0; t < 1; t++){
//             JSONObject newsTitleDict = jArray.getJSONObject(t);
//              
//           newsList.add(newsTitleDict.getString("title"));
//           newsList2.add(newsTitleDict.getString("title"));
//             
//         }
//         


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


}   



public void loadLastResults(){

resultsFeedRequest = "1.0/league-website/" + chosenLeagueId + "/results?month=04&team_id=" + ChosenTeamId + "&limit=31&timestamp=" + unixTimeStamp;
resultsFeedURL = "https://www.website.com/_services/api/" + resultsFeedRequest; 

String myhash = buildHmacSignature(apiKey, resultsFeedURL);

Date anotherCurDate = new Date();  
SimpleDateFormat formatter = new SimpleDateFormat("MM");  
String CurMonth = formatter.format(anotherCurDate);  

int Int = Integer.parseInt(CurMonth);

int MonthInt = Int -1;





CurMonth = (String) (String.valueOf(MonthInt));



if (CurMonth.equals("1")){

    lastMonth = "12";

}    
 else {
    if(CurMonth.length() < 2){
        lastMonth = "0" + CurMonth;
    } else {
        lastMonth = CurMonth;
    }
}





Log.v("lc","month= " + CurMonth);
Log.v("lc","LastMonth= " + lastMonth);


    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(resultsFeedURL);

    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    pairs.add(new BasicNameValuePair("requestToken", myhash));
    pairs.add(new BasicNameValuePair("apiUser", apiUser));

    try {
        post.setEntity (new UrlEncodedFormEntity(pairs));
        HttpResponse response = client.execute(post);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        fulldata = String.valueOf(json);
        Log.v("myApp","resultsdata" + fulldata);

        newsList = new ArrayList<String>();
        newsList2 = new ArrayList<String>();
        newsList3 = new ArrayList<String>();
        imageList = new ArrayList<String>();


        JSONObject obj = new JSONObject(json);    
        JSONObject objData = obj.getJSONObject("data");
        JSONArray jArray = objData.getJSONArray("results");


           for(int t = 0; t < 1; t++){

               resultsDict = jArray.getJSONObject(t);
              HomeTeam = resultsDict.getString("hometeam");
              AwayTeam = resultsDict.getString("awayteam");
              HomeScore = resultsDict.getString("homescore");
              AwayScore = resultsDict.getString("awayscore");
              Attendance = resultsDict.getString("attendance");
              Division = resultsDict.getString("division");


              Log.v("lc","hometeam" + HomeTeam);
              Log.v("lc","awayteam" + AwayTeam);


           }

           resultsView = LayoutInflater.from(getBaseContext()).inflate(R.layout.resultscell,
                     null);

           TextView homeTeam = (TextView) resultsView.findViewById(R.id.HomeTeam);
           homeTeam.setText(HomeTeam);

           TextView awayTeam = (TextView) resultsView.findViewById(R.id.AwayTeam);
           awayTeam.setText(AwayTeam);

           TextView homeScore = (TextView) resultsView.findViewById(R.id.HomeScore);
           homeScore.setText(HomeScore);

           TextView awayScore = (TextView) resultsView.findViewById(R.id.AwayScore);
           awayScore.setText(AwayScore);

           TextView attendance = (TextView) resultsView.findViewById(R.id.Attendence);
           attendance.setText("Att:" + Attendance);

           TextView division = (TextView) resultsView.findViewById(R.id.Division);
           division.setText(Division);






          Log.v("lc", "resultsDict" + resultsDict);





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


}   




    public void loadnews(){

         newsFeedRequest = "1.0/league-website/" + chosenLeagueId + "/news?timestamp=" + unixTimeStamp;
         newsFeedURL = "https://www.website.com/_services/api/" + newsFeedRequest;  

    String myhash = buildHmacSignature(apiKey, newsFeedURL);



    Log.v("lc" , "resultsurl=" + resultsFeedURL);
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(newsFeedURL);

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
        newsList2 = new ArrayList<String>();
        newsList3 = new ArrayList<String>();
        imageList = new ArrayList<String>();


        JSONObject obj = new JSONObject(json);    
        JSONObject objData = obj.getJSONObject("data");
        JSONArray jArray = objData.getJSONArray("news");
        Log.v("lc","newsAmount= " + newsAmount);

           for(int t = 0; t < newsAmount; t++){
               JSONObject newsTitleDict = jArray.getJSONObject(t);
               imageList.add(newsTitleDict.getString("image_small"));
             newsList3.add(newsTitleDict.getString("title"));

           }

           for(int t = 0; t < 1; t++){
               JSONObject newsTitleDict = jArray.getJSONObject(t);

         newsList.add(newsTitleDict.getString("title"));
//           newsList2.add(newsTitleDict.getString("title"));

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


}

public void mergeAdapterSetup(){    


    arrayAdapter = new ArrayAdapter<String>(this, R.layout.single_item, newsList);
    arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.resultscell, newsList2);
    //arrayAdapter3 = new ArrayAdapter(this, R.layout.complex_item, newsList3);



     String[] mStrings = (String[]) imageList.toArray(new String[imageList.size()]);
     String[] news = (String[]) newsList3.toArray(new String[newsList3.size()]);




     arrayAdapter3 = new LazyAdapter(this, mStrings, news);


        ListView list = getListView();
           list.setTextFilterEnabled(true);

           LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            View header = inflater.inflate( R.layout.homeheader, list, false);
            View header2 = inflater.inflate( R.layout.homeheader2, list, false);
            View header3 = inflater.inflate( R.layout.homeheader3, list, false);


    //setListAdapter (arrayAdapter);


        adapter = new MergeAdapter();
        adapter.addView(header);
        adapter.addAdapter(arrayAdapter);
        adapter.addView(header2);
        adapter.addView(resultsView);
        adapter.addView(header3);
        adapter.addAdapter(arrayAdapter3);
        setListAdapter(adapter);
}

public void loadDataLeague(){

    String myhash = buildHmacSignature(apiKey, newsFeedURL);


    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(newsFeedURL);

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
        newsList2 = new ArrayList<String>();
        newsList3 = new ArrayList<String>();
        imageList = new ArrayList<String>();


        JSONObject obj = new JSONObject(json);    
        JSONObject objData = obj.getJSONObject("data");
        JSONArray jArray = objData.getJSONArray("news");
        Log.v("lc","newsAmount= " + newsAmount);

           for(int t = 0; t < newsAmount; t++){
               JSONObject newsTitleDict = jArray.getJSONObject(t);
               imageList.add(newsTitleDict.getString("image_small"));
             newsList3.add(newsTitleDict.getString("title"));

           }

           for(int t = 0; t < 1; t++){
               JSONObject newsTitleDict = jArray.getJSONObject(t);

             newsList.add(newsTitleDict.getString("title"));
             newsList2.add(newsTitleDict.getString("title"));

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


    arrayAdapter = new ArrayAdapter<String>(this, R.layout.single_item, newsList);
    arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.single_item, newsList2);
    //arrayAdapter3 = new ArrayAdapter(this, R.layout.complex_item, newsList3);

     String[] mStrings = (String[]) imageList.toArray(new String[imageList.size()]);
     String[] news = (String[]) newsList3.toArray(new String[newsList3.size()]);


     arrayAdapter3 = new LazyAdapter(this, mStrings, news);


        ListView list = getListView();
           list.setTextFilterEnabled(true);

           LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            View header3 = inflater.inflate( R.layout.homeheader3, list, false);


    //setListAdapter (arrayAdapter);


        adapter = new MergeAdapter();
        adapter.addView(header3);
        adapter.addAdapter(arrayAdapter3);
        setListAdapter(adapter);

    }   

    public void loadData(){
        loadresults();

        loadnews();
        mergeAdapterSetup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        PostTask jsontask;
        jsontask = new PostTask();
        jsontask.execute();





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
