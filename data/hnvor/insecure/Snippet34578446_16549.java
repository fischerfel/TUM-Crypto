package sample.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Date;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {


    public interface Constants {
        String LOG = "sample.app";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressDialog mProgressDialog;
       Log.e(Constants.LOG , "we are here");

        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     new Status().execute();
    }
});

    }

  public class Status extends AsyncTask<Void , Void , Void>{

      ProgressDialog mProgressDialog;
      public String value = null;

      @Override
      protected void onPreExecute() {
          super.onPreExecute();

          EditText userName = (EditText) findViewById(R.id.userName);
          EditText passwd = (EditText) findViewById(R.id.editText);
          String user = (String)userName.getText().toString();
          String pass = (String)passwd.getText().toString();
      }

      @Override
      protected Void doInBackground(Void... params){
          SSLContext ctx = null;
          try {
              ctx = SSLContext.getInstance("TLS");
          } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
          }
          try {
              ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
          } catch (KeyManagementException e) {
              e.printStackTrace();
          }

          SSLContext.setDefault(ctx);
          URL url = null;
          HttpsURLConnection conn = null;
          try {
              url = new URL("https://example.com");
          } catch (MalformedURLException e) {
              e.printStackTrace();
          }

          try{
               conn = (HttpsURLConnection)url.openConnection();
              conn.setHostnameVerifier(new HostnameVerifier() {
                  @Override
                  public boolean verify(String arg0, SSLSession arg1) {
                      return true;
                  }
              });
          }
          catch (IOException io){

          }   
          try {

              BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
              String input = org.apache.commons.io.IOUtils.toString(br);
              Document doc = Jsoup.parse(input);
              Date d = new Date();
              String loginTime = doc.select("input#loginTime").val();

              value = loginTime;

          } catch (IOException e) {
              e.printStackTrace();

          }

          conn.disconnect();
          return null;




      }

      @Override
      protected void onPostExecute(Void aVoid) {

          if(value  == null){
              value = "Nothing";
          }
          Toast toast = Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG);
          toast.show();
      }

    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
