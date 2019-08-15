package com.ninja.mock.ninjamockapplication.parser;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shreeya Patel on 12/28/2015.
 */

public class JSONParser {

    static String SITE_URL = "https://api.otentik.id/post/";

    static String url;
    static String result;
    static HttpPost httpPost;
    static HttpClient httpClient;
    static JSONObject jsonObject;
    static JSONArray jsonArray;
    static HttpEntity httpEntity;
    static InputStream is;
    static HttpResponse httpResponse;
    static List<NameValuePair> pairs;
    static String json = "";


/**
     * Initialization of Variables
     */

    private static void init() {
        // TODO Auto-generated constructor stub
        result = "";
        jsonObject = null;
        httpPost = null;
        is = null;
        jsonArray = null;
        httpClient = getNewHttpClient();
    }

    private static String getResult(InputStream is) {
        BufferedReader reader;
        StringBuilder stringBuilder = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(is, "iso-8859-1"), 8);

            stringBuilder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            is.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }
    public static JSONObject RegisterNewUser(String UserName,
                                             String Email,
                                             String Password) {
        // TODO Auto-generated method stub

        try {
            init();

            url = SITE_URL + "register/ortu";
            Log.d("TAG", url);
            httpPost = new HttpPost(url.toString());
           /* httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            httpPost.setHeader("Accept", "application/json");*/

            Log.d("TAG", "IN ADD USER JSON PARSING  : " + UserName + "\n" + Email + "\n" + Password);
            Log.d("TAG", "HTTP POST" + httpPost);
            pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("UserName", UserName));
            pairs.add(new BasicNameValuePair("Email", Email));
            pairs.add(new BasicNameValuePair("Password", Password));

            Log.d("TAG", "IN ADD USER JSON PARSING  : " + UserName + "\n" + Email + "\n" + Password);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            Log.d("TAG", "HTTP POST" + httpPost);

            httpResponse = httpClient.execute(httpPost);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            Log.d("TAG", "HTTP RESPONSE Code" + responseCode);
            Log.d("TAG", "HTTP RESPONSE" + httpResponse);
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

/* Convert response to string */

            result = getResult(is);
            Log.d("TAG", "RESULTTTTTTTTTTTTT : " + result);
            jsonObject = new JSONObject(result);
            Log.d("TAG", "JSON OBJECT : " + jsonObject);

        } catch (ClientProtocolException e) {
            Log.e("TAG", "Error in Client Protocol : " + e.toString());
        } catch (JSONException e) {
            Log.e("TAG", "Error Parsing data " + e.toString());
        } catch (Exception e) {
            Log.e("TAG", "Error in HTTP Connection : " + e.toString());
        }
        Log.d("TAG", "JSON OBJECTTTTTTTTTTTTTTTTTT : " + jsonObject);
        return jsonObject;
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }

    }
}
