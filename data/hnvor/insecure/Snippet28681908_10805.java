package com.example.gnr_p_v2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by Diogo on 10/02/2015.
 */
public class Exportar_BD_2 extends Activity {


    MultipartEntity reqEntity;
    EditText ed;
    String nome_fich;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_de_dados);

        ed = (EditText) findViewById(R.id.edit);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //sendFile("https://montemor.primelayer.pt/ws/inteste.php");
        try {
            trustAllHosts();
            faz_ligacao();
            send_image();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public void send_image(){


        Bundle extras = getIntent().getExtras();

        String nome_screen = extras.getString("screen");


        File file = new File(nome_screen);
        FileBody fileBody = new FileBody(file);

        reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("file", fileBody);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                HttpsURLConnection url = null;

                try {
                    url = (HttpsURLConnection) new URL("https://montemor.primelayer.pt/ws/sendpic.php").openConnection();
                    url.setHostnameVerifier(DO_NOT_VERIFY);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.err.println("Abri conexão!");


                HttpClient httpclient = new DefaultHttpClient();

                String encoding = new String(
                        org.apache.commons.codec.binary.Base64.encodeBase64
                                (org.apache.commons.codec.binary.StringUtils.getBytesUtf8("montemor:mx89wen3%j3h2bjd098@"))
                );

                HttpPost httppost = new HttpPost("https://montemor.primelayer.pt/ws/sendpic.php");

                httppost.setHeader("Authorization", "Basic " + encoding);

                httppost.setEntity(reqEntity);


                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {

                    String responseStr = null;
                    try {
                        responseStr = EntityUtils.toString(resEntity).trim();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.v("TAG", "Response: " + responseStr);

                    // you can add an if statement here and do other actions based on the response
                }

                return null;
            }
        }.execute();

    }

    public void faz_ligacao() throws KeyManagementException, NoSuchAlgorithmException {



        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                HttpsURLConnection url = null;

                try {
                    url = (HttpsURLConnection) new URL("https://montemor.primelayer.pt/ws/inserirocorrencia.php").openConnection();
                    url.setHostnameVerifier(DO_NOT_VERIFY);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.err.println("Abri conexão!");


                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 100000);
                HttpConnectionParams.setSoTimeout(httpParameters, 100000);
                HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

                //Thread safe in case various AsyncTasks try to access it concurrently
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParameters, schemeRegistry);

                HttpClient httpclient = new DefaultHttpClient(cm, httpParameters);

                String encoding = new String(
                        org.apache.commons.codec.binary.Base64.encodeBase64
                                (org.apache.commons.codec.binary.StringUtils.getBytesUtf8("montemor:mx89wen3%j3h2bjd098@"))
                );

                Intent intent = getIntent();

                Bundle extras = intent.getExtras();
                ArrayList<String> lista_form_campo = extras.getStringArrayList("array_campo");
                ArrayList<String> lista_form_valor = extras.getStringArrayList("array_valor");


                HttpPost httppost = new HttpPost("https://montemor.primelayer.pt/ws/inserirocorrencia.php");
                //https://montemor.primelayer.pt/ws/inteste.php?id=22&desig=teste2
                //httppost.setHeader("User-Agent", "MySuperUserAgent");
                httppost.setHeader("Authorization", "Basic " + encoding);

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                int precorre = 0;

                for (; precorre < lista_form_campo.size(); precorre++) {
                    System.err.println(lista_form_campo.get(precorre) + " " + lista_form_valor.get(precorre));
                    nameValuePairs.add(new BasicNameValuePair(lista_form_campo.get(precorre).toString(), lista_form_valor.get(precorre).toString()));
                }


                try {
                    UrlEncodedFormEntity url_e = new UrlEncodedFormEntity(nameValuePairs);
                    System.err.println("URL ENCODE: " + url_e);
                    httppost.setEntity(url_e);
                    System.err.println("Request Line: " + httppost.getRequestLine());
                    System.err.println("Method: " + httppost.getMethod());
                    System.err.println("Request Line: " + httppost.getParams());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                url.setDoOutput(true);
                url.setInstanceFollowRedirects(false);

                try {
                    url.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                url.setRequestProperty("Content-Type", "text/plain");
                url.setRequestProperty("charset", "utf-8");

                try {
                    url.connect();
                    System.err.println("Conectei e bem!");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Não conectei");
                }


                try {
                    HttpResponse resp = httpclient.execute(httppost);
                    System.err.println(httppost.getURI());
                    System.err.println("" + resp.getStatusLine().getStatusCode());
                    System.err.println("Entity: " + resp.getEntity());
                    System.err.println("Executei o Post ");
                } catch (IOException e) {
                    System.err.println("Não executei o Post");
                    e.printStackTrace();
                }


                return null;
            }

        }.execute();

    }


    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    /**
     * Trust every server - dont check for any certificate
     */

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
