package com.ocforums.application;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.TagNode;


import android.app.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.ocforums.application.R;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;




public class OCForumsAppActivity extends Activity {


    //private MyCustomAdapter mAdapter;


    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void loginfunc(String un,String pwd) {
         try {
             String pmd5sum = md5(pwd);
             //HttpClient client = new DefaultHttpClient(); 
             DefaultHttpClient client = new DefaultHttpClient();

             HttpGet httpget = new HttpGet("https://portal.sun.com/portal/dt");
             HttpResponse response = client.execute(httpget);
             HttpEntity entity = response.getEntity();
             List<Cookie> cookies = client.getCookieStore().getCookies();
             String postURL = "http://www.overclockers.com/forums/login.php?do=login";
             HttpPost post = new HttpPost(postURL);
                 List<NameValuePair> params = new ArrayList<NameValuePair>();
                 params.add(new BasicNameValuePair("vb_login_username", un));
                 params.add(new BasicNameValuePair("vb_login_md5sumpassword", pmd5sum));
                 params.add(new BasicNameValuePair("cookieuser","1"));
                 UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
                 post.setEntity(ent);
                 response = client.execute(post);
                 entity = response.getEntity();
                 cookies = client.getCookieStore().getCookies();

                 if (entity != null) {    
                     Log.i("RESPONSE",EntityUtils.toString(entity));
                 }
                 client.getConnectionManager().shutdown();
         } catch (Exception e) {
             e.printStackTrace();
         }


    }

    /* private class MyCustomAdapter extends BaseAdapter {

            private ArrayList mData = new ArrayList();
            private LayoutInflater mInflater;

            public MyCustomAdapter() {
                mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            public void addItem(final List<List<String>> output2d) {
                mData.add(output2d);
                notifyDataSetChanged();
            }


            public int getCount() {
                return mData.size();
            }


            public String getItem(int position) {
                return (String) mData.get(position);
            }


            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                System.out.println("getView " + position + " " + convertView);
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.id.listView1, null);
                    holder = new ViewHolder();
                    holder.textView = (TextView)convertView.findViewById(R.id.listView1);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder)convertView.getTag();
                }
                holder.textView.setText((CharSequence) mData.get(position));
                return convertView;
            }

        }

        public static class ViewHolder {
            public TextView textView;
        }*/


      public static String gethurl()
      {
         return OCForumsAppActivity.hurl;
      }
      public static void  sethurl(String newh)
      {
          OCForumsAppActivity.hurl = newh;
      }
      public static void sethreflist(List<String> newlist)
      {
          hrefslist = newlist;
      }
    CustomListView lv = new CustomListView();
    public static String hurl;
    private ProgressDialog pd;
    static List<String> hrefslist;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        pd = ProgressDialog.show(OCForumsAppActivity.this, "Working...", "request to server", true, false);
        new ParseForums().execute("http://www.overclockers.com/forums/?styleid=23");


        ListView list = (ListView)findViewById(R.id.listView1);
        list.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

             Log.i("testing",hurl); 

             if(hurl.matches("(?i).*forumdisplay.*")){

                 Intent newActivity = new Intent(getBaseContext(), BothActivity.class);     
                 startActivity(newActivity);


             }
             else if(hurl.matches("(?i).*showthread.php.*")) {
                 Intent newActivity = new Intent(getBaseContext(), ThreadActivity.class);     
                 startActivity(newActivity);
             }
             else if(hrefslist == null){
                 Toast.makeText(getApplicationContext(), "Confused?", Toast.LENGTH_LONG).show();
                 //TODO add context menu for quote and reply
             }
             else{
                 Toast.makeText(getApplicationContext(), "Very Confused", Toast.LENGTH_LONG).show();
             }
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
        switch (item.getItemId()) {

          case R.id.login:    
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LinearLayout lila1= new LinearLayout(this);
            lila1.setOrientation(1); //1 is for vertical orientation
            final EditText usernametextbox = new EditText(this);
            final EditText passwdtextbox = new EditText(this);
            lila1.addView(usernametextbox);
            lila1.addView(passwdtextbox);
            alert.setView(lila1);
            alert.setTitle("Login");
            alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String uname = usernametextbox.getText().toString().trim();
                    String passwd = usernametextbox.getText().toString().trim();
                    loginfunc(uname,passwd);
                }
            });

            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            alert.show();
         break;
         case R.id.settings:
             Toast.makeText(getApplicationContext(), "Perhaps one day...", Toast.LENGTH_SHORT).show();
         break;   

         case R.id.UnansweredThreads:
             hurl ="http://www.overclockers.com/forums/search.php?do=process&replyless=1&replylimit=0&exclude=78,124,37,167,186,186,187,21,144,18,179,150,181,182,183,11,164,95,151,123,27,28,29,30,31,32,142,170,33,36,67,62,63,65,11,19,200&nocache=0";
             Intent newActivity = new Intent(getBaseContext(), ThreadActivity.class);     
             startActivity(newActivity);
         break;   

         case R.id.MyPosts:
             hurl = "http://www.overclockers.com/forums/search.php?do=getdaily&exclude=123&nocache=1";
             Intent newActivity1 = new Intent(getBaseContext(), ThreadActivity.class);     
             startActivity(newActivity1);
         break;   


        }
        return true;
    }



    class ParseForums extends AsyncTask<String, Void, List<List<String>>> {

        protected List<List<String>> doInBackground(String... arg) {
            List<List<String>> combined2d = new ArrayList<List<String>>();
            List<String> output = new ArrayList<String>();
            List<String> hrefs = new ArrayList<String>();

            try
            {
                HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
                List<TagNode> links = hh.getLinksByClass("forumtitle");

                for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext();)
                {
                    TagNode divElement = (TagNode) iterator.next();
                    output.add(divElement.getText().toString());
                    hrefs.add(divElement.getAttributeByName("href").toString());
                }

                combined2d.add(output);
                combined2d.add(hrefs);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return combined2d;
        }

        protected void onPostExecute(List<List<String>> output2d) {

            pd.dismiss();
            output2d.size();
            Log.i("size",Integer.toString(output2d.get(0).size()));
            Log.i("size",Integer.toString(output2d.get(1).size()));
            ListView listview = (ListView) findViewById(R.id.listView1);

            listview.setAdapter(lv.new MyCustomAdapter(OCForumsAppActivity.this, R.layout.row , output2d.get(0)));



        hrefslist = output2d.get(1);
        }
        }




    }
