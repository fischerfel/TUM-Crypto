package com.example.instaemgnew.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.instaemgnew.HomeActivity;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

public class beitragLoader extends AsyncTask<String, String, String>{

    //Array List für die Beiträge
    ArrayList<Beitrag> beitraege;

    //User Daten
    /*mail = userManager.getMail();
    grade = String.valueOf(userManager.getGrade());
    school = userManager.getSchool();*/
    String mail = "simon-frey@gmx.de";
    String grade = String.valueOf(334);
    String school = "EMG";

    //JSONParser
    JSONParser jsonParser = new JSONParser();

    //ArrayList mit Beitrag Objekten
    ArrayList<Beitrag> beitraegeList;

    // Onlinedaten
    private static final String SERVER_URL = "http://yooui.de/InstaEMGTest/";
    private static final String PASSWORD = "8615daf406f7e2b313494f0240";

    //Context
    private final HomeActivity homeActivity;

    //Konstruktor
    public beitragLoader(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
         Log.e("TestPoint 2", "Created beitragLoader");

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TODO: Test for InternetConnection
         Log.e("TestPoint 3", "PreExectute");

    }

    /**
     * getting All products from url
     * */
    protected String doInBackground(String... args) {
        beitraegeList = new ArrayList<Beitrag>();
        String SQLUrl = SERVER_URL + "testBeiträgeAbrufen.php";
        String token = getMD5Hash("password" + "data");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        //TODO: params.add(new BasicNameValuePair("page", skipBeitraege))    
        params.add(new BasicNameValuePair("grade", grade));
        params.add(new BasicNameValuePair("school", school));

        JSONObject json = jsonParser.makeHttpRequest(SQLUrl, "GET", params);


        if (json == null) {
            // Server offline

        }

         Log.e("TestPoint 3,5", "FetchedJSON");


        try {
            JSONArray beitraege = json.getJSONArray("beitraege");


             // looping through All Products
            for (int i = 0; i < beitraege.length(); i++) {
                Beitrag tempBeitrag = null;

                 Log.e("TestPoint 3,6", "StartLoop");

                JSONObject c = beitraege.getJSONObject(i);

                //HDImagesURLList ArrayList
                ArrayList<String> HDImagesURLList = new ArrayList<String>();

                // Storing each json item in variable
                String id = c.getString("ID");
                String url = c.getString("url");
                String titel = c.getString("titel");
                String tags = c.getString("tags");
                String onlineDate = c.getString("onlineDate");

                 Log.e("TestPoint 3,7", "Stored JSON Items");


                //Fetching previewImage
                try {
                     Log.e("TestPoint 3,8", "TryImageDownload");

                InputStream in = new java.net.URL(url).openStream();
                String fileName = "InstaEMG" + String.valueOf(System.currentTimeMillis())+".jpg";

                 Log.e("imageUri", url);
                 Log.e("fileName", fileName);


                FileOutputStream fileOutput = new FileOutputStream(new File(Environment.getExternalStorageDirectory(),fileName));

                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = in.read(buffer)) > 0 ) {
                      fileOutput.write(buffer, 0, bufferLength);
                      Log.e("File Output", String.valueOf(bufferLength));
                }


               //Fill HDImagesURLList
               //TODO


                // creating newBeitrag
               tempBeitrag = new Beitrag(Integer.parseInt(id), titel, onlineDate,  fileName, HDImagesURLList);




                // adding Beitrag to ArrayList
                beitraegeList.add(tempBeitrag);

             Log.e("TestPoint 4", "NewBeitragSet");

                } catch (MalformedURLException e) {

                     Log.e("Exceptrion", "URL Exception");

                } catch (IOException e) {
                     Log.e("Exceptrion", "IO Exception");

                }
                homeActivity.addToListView(tempBeitrag);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        return null;       
    }

    /**
     * After completing background Safe to MainActivity
     * **/
    protected void onPostExecute() {
         Log.e("TestPoint 5", "PostExecutre");

        // homeActivity.updateListView(beitraegeList);

    }

    /**
     * Methode zum Errechnen eines MD5Hashs
     * 
     * @param string
     *            String welcher kodiert werden soll
     * @return MD5 Hash des Strings, bei Fehler der ursprüngliche String.
     */
    private String getMD5Hash(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(string.getBytes());
            byte[] result = md5.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                if ((0xff & result[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & result[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & result[i]));
                }
            }
            string = hexString.toString();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }

        return string;
    };

}
